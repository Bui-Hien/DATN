package com.buihien.datn.controller;

import com.buihien.datn.dto.SalaryPeriodDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/salary-period")
@RestController
public class RestSalaryPeriodController extends GenericApi<SalaryPeriodDto, SearchDto> {

    public RestSalaryPeriodController(GenericService<SalaryPeriodDto, SearchDto> genericService) {
        super(SalaryPeriodDto.class, genericService);
    }
}
