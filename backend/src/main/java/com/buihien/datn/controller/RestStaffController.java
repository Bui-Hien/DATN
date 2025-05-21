package com.buihien.datn.controller;

import com.buihien.datn.dto.StaffDto;
import com.buihien.datn.dto.search.StaffSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/staff")
@RestController
public class RestStaffController extends GenericApi<StaffDto, StaffSearchDto> {

    public RestStaffController(GenericService<StaffDto, StaffSearchDto> genericService) {
        super(StaffDto.class, genericService);
    }
}
