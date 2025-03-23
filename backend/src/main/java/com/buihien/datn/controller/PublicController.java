package com.buihien.datn.controller;

import com.buihien.datn.dto.test.AddressDTO;
import com.buihien.datn.dto.test.EmployeeDTO;
import com.buihien.datn.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@Validated
public class PublicController {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    List<EmployeeDTO> employees = new ArrayList<>();

    //http://localhost:8080/api/public/export-excel
    @GetMapping("/export-excel")
    public ResponseEntity<?> handleExportAdministrativeUnit(HttpServletResponse response) {
        for (int i = 0; i < 1000; i++) {
            EmployeeDTO employee = new EmployeeDTO(
                    (long) i,
                    "First name " + i,
                    "Last name " + i,
                    LocalDate.now(),
                    "Nam",
                    "Email " + i,
                    "Phone " + i,
                    new AddressDTO(
                            "Đường " + i,
                            "Thành phố " + i,
                            "Tiểu bang " + i,
                            "Mã bưu kiện " + i,
                            "Quốc gia " + i
                    )
            );
            this.employees.add(employee);
        }
        ByteArrayResource excelFile;
        if (true) {
            try {
                // Cấu hình response header
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.addHeader("Content-Disposition", "attachment; filename=DU_LIEU_DON_VI_HANH_CHINH.xlsx");

                Instant startFetch = Instant.now(); // Bắt đầu tính thời gian lấy dữ liệu
                excelFile = ExcelUtil.writeExcel(this.employees, EmployeeDTO.class);
                Instant endFetch = Instant.now(); // Kết thúc lấy dữ liệu
                log.info("Finished write data. Time: {} m", Duration.between(startFetch, endFetch).toMillis() / 1000);

                InputStream ins = null;
                if (excelFile != null) {
                    ins = new ByteArrayInputStream(excelFile.getByteArray());
                }
                if (ins != null) {
                    org.apache.commons.io.IOUtils.copy(ins, response.getOutputStream());
                }
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                log.error("Error generating Excel file.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error generating Excel file.");
            }
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/import-excel")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Instant startFetch = Instant.now(); // Bắt đầu tính thời gian lấy dữ liệu
            this.employees = ExcelUtil.readExcel(inputStream, EmployeeDTO.class);
            Instant endFetch = Instant.now(); // Kết thúc lấy dữ liệu
            log.info("Finished read data. Time: {} m", Duration.between(startFetch, endFetch).toMillis() / 1000);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error reading Excel file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading Excel file");
        }
    }
}
