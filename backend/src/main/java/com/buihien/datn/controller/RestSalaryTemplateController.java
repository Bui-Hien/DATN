package com.buihien.datn.controller;

import com.buihien.datn.dto.SalaryTemplateDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/salary-template")
@RestController
public class RestSalaryTemplateController extends GenericApi<SalaryTemplateDto, SearchDto> {

    public RestSalaryTemplateController(GenericService<SalaryTemplateDto, SearchDto> genericService) {
        super(SalaryTemplateDto.class, genericService);
    }
}
