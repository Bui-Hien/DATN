package com.buihien.datn.controller;

import com.buihien.datn.dto.StaffLabourAgreementDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/staff-labour-agreement")
@RestController
public class RestStaffLabourAgreementController extends GenericApi<StaffLabourAgreementDto, SearchDto> {

    public RestStaffLabourAgreementController(GenericService<StaffLabourAgreementDto, SearchDto> genericService) {
        super(StaffLabourAgreementDto.class, genericService);
    }
}
