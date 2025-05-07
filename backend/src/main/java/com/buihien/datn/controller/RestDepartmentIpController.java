package com.buihien.datn.controller;

import com.buihien.datn.dto.DepartmentIpDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/department-ip")
@RestController
public class RestDepartmentIpController extends GenericApi<DepartmentIpDto, SearchDto> {

    public RestDepartmentIpController(GenericService<DepartmentIpDto, SearchDto> genericService) {
        super(DepartmentIpDto.class, genericService);
    }
}
