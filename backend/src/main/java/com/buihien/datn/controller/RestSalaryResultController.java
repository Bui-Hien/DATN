package com.buihien.datn.controller;

import com.buihien.datn.dto.SalaryResultDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/salary-result")
@RestController
public class RestSalaryResultController extends GenericApi<SalaryResultDto, SearchDto> {

    public RestSalaryResultController(GenericService<SalaryResultDto, SearchDto> genericService) {
        super(SalaryResultDto.class, genericService);
    }
}
