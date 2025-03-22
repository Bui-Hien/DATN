package com.buihien.datn.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportExcelUtil {
//    public static ByteArrayResource handleExcelStaffLabourAgreements(List<StaffLabourAgreementDto> datas, SearchStaffLabourAgreementDto searchDto, XSSFWorkbook workbook) throws IOException {
//        if (datas == null || datas.isEmpty()) return null;
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        Font font = workbook.getFontAt((short) 0);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        XSSFCellStyle stringStyle = workbook.createCellStyle();
//        stringStyle.setFont(font);
//        XSSFCellStyle cellStyle = workbook.createCellStyle();
//        cellStyle.setBorderBottom(BorderStyle.THIN);
//        cellStyle.setBorderTop(BorderStyle.THIN);
//        cellStyle.setBorderLeft(BorderStyle.THIN);
//        cellStyle.setBorderRight(BorderStyle.THIN);
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setWrapText(true);
//        cellStyle.setFont(font);
//        XSSFRow row = sheet.getRow(3);
//        if (row == null) {
//            row = sheet.createRow(3); // Tạo mới nếu dòng không tồn tại
//        }
//
//        XSSFCell cell = row.getCell(2); // Cột C = index 2
//        if (cell == null) {
//            cell = row.createCell(2); // Tạo ô nếu chưa tồn tại
//        }
//        String exportType = "";
//        if (searchDto == null || searchDto.getExportType() == null) return null;
//        int type = searchDto.getExportType();
//        if (type == HrConstants.StaffHasSocialInsuranceExportType.INCREASE_2007.getValue()) {
//            exportType = HrConstants.StaffHasSocialInsuranceExportType.INCREASE_2007.getName();
//        } else if (type == HrConstants.StaffHasSocialInsuranceExportType.INCREASE_97_2003.getValue()) {
//            exportType = HrConstants.StaffHasSocialInsuranceExportType.INCREASE_97_2003.getName();
//        } else if (type == HrConstants.StaffHasSocialInsuranceExportType.DECREASE_2007.getValue()) {
//            exportType = HrConstants.StaffHasSocialInsuranceExportType.DECREASE_2007.getName();
//        } else if (type == HrConstants.StaffHasSocialInsuranceExportType.DECREASE_97_2003.getValue()) {
//            exportType = HrConstants.StaffHasSocialInsuranceExportType.DECREASE_97_2003.getName();
//        } else if (type == HrConstants.StaffHasSocialInsuranceExportType.MODIFY_2007.getValue()) {
//            exportType = HrConstants.StaffHasSocialInsuranceExportType.MODIFY_2007.getName();
//        } else if (type == HrConstants.StaffHasSocialInsuranceExportType.MODIFY_97_2003.getValue()) {
//            exportType = HrConstants.StaffHasSocialInsuranceExportType.MODIFY_97_2003.getName();
//        }
//        cell.setCellValue(exportType); // Đặt giá trị cho ô hợp nhất
//
//        List<List<Object>> tempData = new ArrayList<>();
//        for (int rowIndex = 17; rowIndex < 24; rowIndex++) {
//            XSSFRow rowTemp = sheet.getRow(rowIndex);
//            List<Object> rowData = new ArrayList<>();
//            for (int cn = 0; cn < rowTemp.getLastCellNum(); cn++) {
//                XSSFCell cellTemp = rowTemp.getCell(cn);
//                Map<String, Object> cellInfo = new HashMap<>();
//                cellInfo.put("value", cellTemp.toString());
//                cellInfo.put("style", cellTemp.getCellStyle());
//                rowData.add(cellInfo);
//            }
//            tempData.add(rowData);
//        }
//
//        for (int i = 17; i < 24; i++) {
//            XSSFRow removingRow = sheet.getRow(i);
//            if (removingRow != null) {
//                sheet.removeRow(removingRow);
//            }
//        }
//
//        int rowIndex = 6;
//        int index = 0;
//
//        for (StaffLabourAgreementDto item : datas) {
//            if (index > 0) {
//                ++rowIndex;
//            }
//
//            ++index;
//            row = sheet.createRow(rowIndex);
//            sheet.getRow(rowIndex + 1);
//            if (row != null) {
//                //STT
//                cell = row.createCell(0);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue((double) index);
//                //LoaiD02
//                cell = row.createCell(1);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue(exportType);
//                //HoTen
//                cell = row.createCell(2);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getDisplayName() != null) {
//                    cell.setCellValue(item.getStaff().getDisplayName());
//                } else {
//                    cell.setCellValue("");
//                }
//
////                MaSoBHXH
//                cell = row.createCell(3);
//                cell.setCellStyle(cellStyle);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
//                if (item.getStaff() != null && item.getStaff().getContractNumber() != null) {
//                    cell.setCellValue(item.getStaff().getContractNumber());
//                } else {
//                    cell.setCellValue("");
//                }
//
////                SoSoBHXH
//                cell = row.createCell(4);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
//                if (item.getStaff() != null && item.getStaff().getSocialInsuranceNumber() != null) {
//                    cell.setCellValue(item.getStaff().getSocialInsuranceNumber());
//                } else {
//                    cell.setCellValue("");
//                }
//
////                SoLanKeKhai
//                cell = row.createCell(5);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//
//                //ThangNamKeKhai
//                cell = row.createCell(6);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//
//                //NguoiLapBieu
//                cell = row.createCell(7);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //ThuTruongDonVi
//                cell = row.createCell(8);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TongSoBHXH
//                cell = row.createCell(9);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TongTheBHYT
//                cell = row.createCell(10);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //ChucVu
//                cell = row.createCell(11);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getCurrentPosition() != null && item.getStaff().getCurrentPosition().getName() != null) {
//                    cell.setCellValue(item.getStaff().getCurrentPosition().getName());
//                } else {
//                    cell.setCellValue("");
//                }
//                //Muc/HeSoLuong
//                cell = row.createCell(12);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getInsuranceSalaryCoefficient() != null) {
//                    cell.setCellValue(item.getStaff().getInsuranceSalaryCoefficient());
//                } else {
//                    cell.setCellValue("");
//                }
//                //PCChucVu
//                cell = row.createCell(13);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //PCThamNienVK
//                cell = row.createCell(14);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //PCThamNienNghe
//                cell = row.createCell(15);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //PCLuong
//                cell = row.createCell(16);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //PCCacKhoanBS
//                cell = row.createCell(17);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TuThangNam
//                cell = row.createCell(18);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getInsuranceStartDate() != null) {
//                    cell.setCellValue(formatter.format(item.getStaff().getInsuranceStartDate()));
//                } else {
//                    cell.setCellValue("");
//                }
//                //DenThangNam
//                cell = row.createCell(19);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getInsuranceEndDate() != null) {
//                    cell.setCellValue(formatter.format(item.getStaff().getInsuranceEndDate()));
//                } else {
//                    cell.setCellValue("");
//                }
//                //PhuongAn
//                cell = row.createCell(20);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //GhiChu
//                cell = row.createCell(21);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TyLeDong
//                cell = row.createCell(22);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaffPercentage() != null) {
//                    cell.setCellValue(item.getStaffPercentage());
//                } else {
//                    cell.setCellValue("");
//                }
//                //PhongBanLamViec
//                cell = row.createCell(23);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getDepartment() != null && item.getStaff().getDepartment().getName() != null) {
//                    cell.setCellValue(item.getStaff().getDepartment().getName());
//                } else {
//                    cell.setCellValue("");
//                }
//                //NoiLamViec
//                cell = row.createCell(24);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
//                if (item.getWorkingPlace() != null) {
//                    cell.setCellValue(item.getWorkingPlace());
//                } else {
//                    cell.setCellValue("");
//                }
//                //MaVungSS
//                cell = row.createCell(25);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //MaLuongTT
//                cell = row.createCell(26);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //LoaiVTLV
//                cell = row.createCell(27);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NgayBatDauVTLV
//                cell = row.createCell(28);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NgayKetThucVTLV
//                cell = row.createCell(29);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//
//                //LoaiHDLD
//                cell = row.createCell(30);
//                cell.setCellStyle(cellStyle);
//                if (item.getLabourAgreementType() != null && item.getLabourAgreementType().getName() != null) {
//                    cell.setCellValue(item.getLabourAgreementType().getName());
//                } else {
//                    cell.setCellValue("");
//                }
//                //NgayBatDauHDLD
//                cell = row.createCell(31);
//                cell.setCellStyle(cellStyle);
//                if (item.getStartDate() != null) {
//                    cell.setCellValue(formatter.format(item.getStartDate()));
//                } else {
//                    cell.setCellValue("");
//                }
//                //NgayKetThucHDLD
//                cell = row.createCell(32);
//                cell.setCellStyle(cellStyle);
//                if (item.getEndDate() != null) {
//                    cell.setCellValue(formatter.format(item.getEndDate()));
//                } else {
//                    cell.setCellValue("");
//                }
//                //NgayBatDauNNDH
//                cell = row.createCell(33);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NgayKetThucNNDH
//                cell = row.createCell(34);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NgaySinh
//                cell = row.createCell(35);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getBirthDate() != null) {
//                    cell.setCellValue(formatter.format(item.getStaff().getBirthDate()));
//                } else {
//                    cell.setCellValue("");
//                }
//                //GioiTinh
//                cell = row.createCell(36);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getGender() != null) {
//                    if (item.getStaff().getGender().equals(Const.GENDER_ENUM.FEMALE.getValue())) {
//                        cell.setCellValue(Const.GENDER_ENUM.FEMALE.getDisplay());
//                    } else {
//                        cell.setCellValue(Const.GENDER_ENUM.MALE.getDisplay());
//                    }
//                } else {
//                    cell.setCellValue("");
//                }
//                //QuocTich
//                cell = row.createCell(37);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getNationality() != null && item.getStaff().getNationality().getName() != null) {
//                    cell.setCellValue(item.getStaff().getNationality().getName());
//                } else {
//                    cell.setCellValue("");
//                }
//
//                //DanToc
//                cell = row.createCell(38);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getEthnics() != null && item.getStaff().getEthnics().getName() != null) {
//                    cell.setCellValue(item.getStaff().getEthnics().getName());
//                } else {
//                    cell.setCellValue("");
//                }
//                //GKSTinh/TP
//                cell = row.createCell(39);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getBirthPlace() != null) {
//                    cell.setCellValue(item.getStaff().getBirthPlace());
//                } else {
//                    cell.setCellValue("");
//                }
//                //GKSHuyen/Quan
//                cell = row.createCell(40);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getBirthPlace() != null) {
//                    cell.setCellValue(item.getStaff().getBirthPlace());
//                } else {
//                    cell.setCellValue("");
//                }
//                //GKSXa/Phuong
//                cell = row.createCell(41);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getBirthPlace() != null) {
//                    cell.setCellValue(item.getStaff().getBirthPlace());
//                } else {
//                    cell.setCellValue("");
//                }
//                //SoCMND
//                cell = row.createCell(42);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
//                if (item.getStaff() != null && item.getStaff().getIdNumber() != null) {
//                    cell.setCellValue(item.getStaff().getIdNumber());
//                } else {
//                    cell.setCellValue("");
//                }
//                //LHSoNha
//                cell = row.createCell(43);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //LHTinh/TP
//                cell = row.createCell(44);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //LHHuyen/Quan
//                cell = row.createCell(45);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //LHXa/Phuong
//                cell = row.createCell(46);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //SoDienThoaiLH
//                cell = row.createCell(47);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null && item.getStaff().getPhoneNumber() != null) {
//                    cell.setCellValue(item.getStaff().getPhoneNumber());
//                } else {
//                    cell.setCellValue("");
//                }
//                //MucTienDong
//                cell = row.createCell(48);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
//                if (item.getTotalInsuranceAmount() != null) {
//                    cell.setCellValue(item.getTotalInsuranceAmount());
//                } else {
//                    cell.setCellValue("");
//                }
//                //PhuongThucDong
//                cell = row.createCell(49);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //MaNoiKCB
//                cell = row.createCell(50);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NDThayDoiYeuCau
//                cell = row.createCell(51);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TLKemTheo
//                cell = row.createCell(52);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NDLapBangKe
//                cell = row.createCell(53);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TKKemTheo
//                cell = row.createCell(54);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TenLoaiVanBan
//                cell = row.createCell(55);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //SoHieuVanBan
//                cell = row.createCell(56);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NgayBanHanh
//                cell = row.createCell(57);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //NgayHieuLuc
//                cell = row.createCell(58);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //CQBanHanhVanBan
//                cell = row.createCell(59);
//                cell.setCellStyle(cellStyle);
//                if (item.getStaff() != null &&
//                        item.getStaff().getDepartment() != null &&
//                        item.getStaff().getDepartment().getOrganization() != null &&
//                        item.getStaff().getDepartment().getOrganization().getName() != null
//                ) {
//                    cell.setCellValue(item.getStaff().getDepartment().getOrganization().getName());
//                } else {
//                    cell.setCellValue("");
//                }
//                //TrichYeuVanBan
//                cell = row.createCell(60);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue("");
////                if () {
////                    cell.setCellValue();
////                } else {
////                    cell.setCellValue("");
////                }
//                //TrichLuocNoiDungCanThamDinh
//                cell = row.createCell(61);
//                cell.setCellStyle(cellStyle);
//                cell.setCellValue(exportType);
//            }
//        }
//        rowIndex += 1;
//        for (List<Object> rowData : tempData) {
//            row = sheet.createRow(++rowIndex);
//            for (int i = 0; i < rowData.size(); i++) {
//                Map<String, Object> cellInfo = (Map<String, Object>) rowData.get(i);
//                cell = row.createCell(i);
//                cell.setCellValue(cellInfo.get("value").toString());
//                cell.setCellStyle((XSSFCellStyle) cellInfo.get("style"));
//            }
//        }
//
//
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        workbook.write(out);
//        workbook.close();
//        return new ByteArrayResource(out.toByteArray());
//    }
//@Secured({HrConstants.ROLE_HR_MANAGEMENT, Constants.ROLE_ADMIN})
//@RequestMapping(path = "/export-excel-staff-social-insurance-by-type", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//public ResponseEntity<?> exportExcelStaffSIByType(@RequestBody SearchStaffLabourAgreementDto dto) {
//    try {
////                    List<StaffLabourAgreementDto> dataList = staffLabourAgreementService.getAll(); //fetch data
//        List<StaffLabourAgreementDto> dataList = staffLabourAgreementService.getAllStaffLabourAgreementWithSearch(dto);
//
//        if (dataList.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//        }
//
//        InputStream inputStream = new ClassPathResource("Excel/XuatDanhsachBHXH.xlsx").getInputStream();
//        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//
//        ByteArrayResource excelFile = ExportExcelUtil.handleExcelStaffLabourAgreements(dataList, dto, workbook);
//
//        if (excelFile == null) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=XuatDanhsachBHXH.xlsx");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(excelFile);
//    } catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//    }
//}
}
