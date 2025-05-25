package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.StaffDto;
import com.buihien.datn.dto.StaffWorkScheduleSummaryDto;
import com.buihien.datn.dto.search.StaffSearchDto;
import com.buihien.datn.dto.search.StaffWorkScheduleSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.StaffService;
import com.buihien.datn.service.StaffWorkScheduleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/staff")
@RestController
public class RestStaffController extends GenericApi<StaffDto, StaffSearchDto> {

    public RestStaffController(GenericService<StaffDto, StaffSearchDto> genericService) {
        super(StaffDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN, DatnConstants.ROLE_USER})
    @GetMapping("/current-staff")
    public ResponseData<StaffDto> getScheduleSummary() {
        StaffDto response = ((StaffService) genericService).getCurrentStaff();
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy thông tin cá nhân", response);
    }
}
