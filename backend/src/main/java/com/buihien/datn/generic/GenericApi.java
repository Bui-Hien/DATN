package com.buihien.datn.generic;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.AuditableDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.util.ExcelUtil;
import com.buihien.datn.util.anotation.Excel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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

@Validated
public class GenericApi<DTO extends AuditableDto, S extends SearchDto> {
    private final Logger log = LoggerFactory.getLogger(GenericApi.class);
    private final Class<DTO> dtoClass;
    private final GenericService<DTO, S> genericService;

    public GenericApi(Class<DTO> dtoClass, GenericService<DTO, S> genericService) {
        this.dtoClass = dtoClass;
        this.genericService = genericService;
    }


    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping(value = "/save-or-update")
    public ResponseData<?> saveOrUpdate(@Valid @RequestBody DTO dto) {
        try {
            DTO result = genericService.saveOrUpdate(dto);
            return new ResponseData<>(
                    dto.getId() == null ? HttpStatus.CREATED.value() : HttpStatus.ACCEPTED.value(),
                    dto.getId() == null ? "Successfully saved" : "Successfully updated",
                    result.getId()
            );
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    dto.getId() == null ? "Failed to save" : "Failed to update");
        }
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping(value = "/save-all")
    public ResponseData<?> saveOrUpdateAll(@Valid @RequestBody List<DTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The input list is empty.");
        }
        try {
            Integer savedCount = genericService.saveOrUpdateList(dtoList);
            log.info("Successfully saved {} records.", savedCount);
            return new ResponseData<>(HttpStatus.OK.value(), "Successfully saved list", savedCount);
        } catch (Exception e) {
            log.error("Error saving list: {}", e.getMessage(), e);
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error saving records.");
        }
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteById(@PathVariable @Min(value = 1) Long id) {
        try {
            genericService.deleteById(id);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete success by id " + id);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete by ID " + id);
        }
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/delete-multiple")
    public ResponseData<?> deleteMultipleByIds(@RequestBody @NotEmpty List<@NotNull @Min(1) Long> ids) {
        try {
            int deletedCount = genericService.deleteMultiple(ids);
            return new ResponseData<>(HttpStatus.OK.value(), "Successfully deleted", deletedCount);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e);
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete multiple");
        }
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/{id}")
    public ResponseData<?> getById(@PathVariable @Min(1) Long id) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get success by id "+ id, genericService.getById(id));
        } catch (ResourceNotFoundException e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Failed to get by ID " + id);
        }
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @GetMapping("/paging")
    public ResponseData<?> paging(@RequestParam(defaultValue = "10") @Min(1) int pageSize,
                                  @RequestParam(defaultValue = "0") @Min(0) int pageIndex) {
        try {
            Page<DTO> page = genericService.paging(pageIndex, pageSize);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", page);
        } catch (Exception e) {
            log.error("Paging error: {}", e.getMessage(), e);
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Paging failed");
        }
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/paging-search")
    public ResponseData<?> pagingSearch(@Valid @RequestBody S dto) {
        try {
            Page<DTO> page = genericService.pagingSearch(dto);
            return new ResponseData<>(HttpStatus.OK.value(), "Success", page);
        } catch (Exception e) {
            log.error("Paging search error: {}", e.getMessage(), e);
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Paging search failed");
        }
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/export-excel")
    public ResponseEntity<?> exportExcel(HttpServletResponse response, @Valid @RequestBody S search) {
        // Bắt đầu đếm thời gian
        Instant startExport = Instant.now();

        // Đánh dấu yêu cầu xuất Excel
        search.setExportExcel(true);

        // Truy vấn danh sách dữ liệu
        Page<DTO> page = genericService.pagingSearch(search);
        List<DTO> datas = page.getContent();

        // Kiểm tra danh sách có dữ liệu không
        if (datas.isEmpty()) {
            log.warn("Export failed: No data available.");
            return ResponseEntity.noContent().build();
        }

        try {
            // Ghi dữ liệu vào file Excel
            ByteArrayResource excelFile = ExcelUtil.writeExcel(datas, dtoClass);
            if (excelFile == null) {
                log.error("Failed to generate Excel file.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to generate the Excel file.");
            }

            // Lấy tên sheet từ annotation @Excel (nếu có)
            Excel excelAnnotation = dtoClass.getAnnotation(Excel.class);
            String sheetName = (excelAnnotation != null && StringUtils.hasText(excelAnnotation.name().strip()))
                    ? excelAnnotation.name()
                    : dtoClass.getSimpleName();

            // Xử lý tên file để tránh lỗi
            String fileName = ExcelUtil.sanitizeFileName(sheetName) + ".xlsx";

            // Cấu hình header cho response để tải file
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // Ghi nội dung file Excel vào response output stream
            try (InputStream ins = new ByteArrayInputStream(excelFile.getByteArray())) {
                org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream());
                response.flushBuffer();

                // Log thời gian hoàn thành xuất file
                Instant endExport = Instant.now();
                log.info("Successfully exported {} records to Excel. Time taken: {} seconds",
                        datas.size(), Duration.between(startExport, endExport).toMillis() / 1000.0);

                return ResponseEntity.ok().build();
            } catch (IOException e) {
                log.error("Error writing Excel file to response: {}", e.getMessage(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error writing Excel file.");
            }
        } catch (Exception e) {
            log.error("Error generating Excel file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating Excel file.");
        }
    }


    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/import-excel")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        // Kiểm tra file rỗng
        if (file.isEmpty()) {
            log.warn("Import failed: File is empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
        }

        // Kiểm tra định dạng file (chỉ chấp nhận .xls và .xlsx)
        String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("").trim();
        if (!(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
            log.warn("Import failed: Invalid file format - {}", fileName);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file format. Only .xls and .xlsx are allowed.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Instant startTime = Instant.now(); // Bắt đầu tính thời gian xử lý

            List<DTO> listData = ExcelUtil.readExcel(inputStream, dtoClass);
            if (listData.isEmpty()) {
                log.warn("Import completed but no valid data found in file: {}", fileName);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No valid data found in the Excel file.");
            }

            // Lưu dữ liệu vào database
            Integer result = genericService.saveOrUpdateList(listData);

            // Tính thời gian xử lý
            double processingTimeSeconds = Duration.between(startTime, Instant.now()).toMillis() / 1000.0;
            log.info("Successfully imported {} records. Processing time: {} seconds", listData.size(), processingTimeSeconds);

            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error("Error reading file {}: {}", fileName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file. Please try again.");
        } catch (IllegalArgumentException e) {
            log.error("Invalid data format in Excel {}: {}", fileName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data format in Excel file.");
        } catch (Exception e) {
            log.error("Unexpected error while importing Excel {}: {}", fileName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


}
