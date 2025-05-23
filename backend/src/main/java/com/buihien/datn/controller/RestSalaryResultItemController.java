package com.buihien.datn.controller;

import com.buihien.datn.dto.SalaryResultItemDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/salary-result-item")
@RestController
public class RestSalaryResultItemController extends GenericApi<SalaryResultItemDto, SearchDto> {

    public RestSalaryResultItemController(GenericService<SalaryResultItemDto, SearchDto> genericService) {
        super(SalaryResultItemDto.class, genericService);
    }
}
