package com.buihien.datn.generic;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.AuditableDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.util.ExcelUtil;
import com.buihien.datn.util.anotation.Excel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
public class GenericApi<DTO extends AuditableDto, S extends SearchDto> {
    private final Logger log = LoggerFactory.getLogger(GenericApi.class);
    private final Class<DTO> dtoClass;
    protected final GenericService<DTO, S> genericService;

    public GenericApi(Class<DTO> dtoClass, GenericService<DTO, S> genericService) {
        this.dtoClass = dtoClass;
        this.genericService = genericService;
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/save-or-update")
    public ResponseData<DTO> saveOrUpdate(@Valid @RequestBody DTO dto) {
        DTO result = genericService.saveOrUpdate(dto);
        return new ResponseData<>(
                dto.getId() == null ? HttpStatus.CREATED.value() : HttpStatus.ACCEPTED.value(),
                dto.getId() == null ? "Successfully saved" : "Successfully updated",
                result
        );
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/save-all")
    public ResponseData<Integer> saveOrUpdateAll(@Valid @RequestBody List<DTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("The input list is empty.");
        }
        Integer savedCount = genericService.saveOrUpdateList(dtoList);
        log.info("Successfully saved {} records.", savedCount);
        return new ResponseData<>(HttpStatus.OK.value(), "Successfully saved list", savedCount);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteById(@PathVariable UUID id) {
        genericService.deleteById(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete success by id " + id);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/delete-multiple")
    public ResponseData<Integer> deleteMultipleByIds(@RequestBody @NotEmpty List<@NotNull UUID> ids) {
        int deletedCount = genericService.deleteMultiple(ids);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Successfully deleted " + ids);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @GetMapping("/{id}")
    public ResponseData<DTO> getById(@PathVariable UUID id) {
        DTO result = genericService.getById(id); // nếu không tồn tại sẽ throw ResourceNotFoundException
        return new ResponseData<>(HttpStatus.OK.value(), "Get success by id " + id, result);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @GetMapping("/paging")
    public ResponseData<Page<DTO>> paging(@RequestParam(defaultValue = "10") @Min(1) int pageSize,
                                          @RequestParam(defaultValue = "0") @Min(0) int pageIndex) {
        Page<DTO> result = genericService.paging(pageIndex, pageSize);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/paging-search")
    public ResponseData<Page<DTO>> pagingSearch(@Valid @RequestBody S dto) {
        Page<DTO> result = genericService.pagingSearch(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/export-excel")
    public ResponseEntity<?> exportExcel(HttpServletResponse response, @Valid @RequestBody S search) throws IOException {
        Instant startExport = Instant.now();

        search.setExportExcel(true);
        Page<DTO> page = genericService.pagingSearch(search);
        List<DTO> datas = page.getContent();

        if (datas.isEmpty()) {
            log.warn("Export failed: No data available.");
            return ResponseEntity.noContent().build();
        }

        ByteArrayResource excelFile = ExcelUtil.writeExcel(datas, dtoClass);
        if (excelFile == null) {
            throw new RuntimeException("Failed to generate the Excel file.");
        }

        Excel excelAnnotation = dtoClass.getAnnotation(Excel.class);
        String sheetName = (excelAnnotation != null && StringUtils.hasText(excelAnnotation.name().strip()))
                ? excelAnnotation.name()
                : dtoClass.getSimpleName();
        String fileName = ExcelUtil.sanitizeFileName(sheetName) + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        try (InputStream ins = new ByteArrayInputStream(excelFile.getByteArray())) {
            org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream());
            response.flushBuffer();

            Instant endExport = Instant.now();
            log.info("Successfully exported {} records to Excel. Time taken: {} seconds",
                    datas.size(), Duration.between(startExport, endExport).toMillis() / 1000.0);

            return ResponseEntity.ok().build();
        }
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/import-excel")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("").trim();
        if (!(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
            throw new IllegalArgumentException("Invalid file format. Only .xls and .xlsx are allowed.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Instant startTime = Instant.now();
            List<DTO> listData = ExcelUtil.readExcel(inputStream, dtoClass);

            if (listData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No valid data found in the Excel file.");
            }

            Integer result = genericService.saveOrUpdateList(listData);

            double processingTimeSeconds = Duration.between(startTime, Instant.now()).toMillis() / 1000.0;
            log.info("Successfully imported {} records. Processing time: {} seconds", listData.size(), processingTimeSeconds);

            return ResponseEntity.ok(result);
        }
    }
}
