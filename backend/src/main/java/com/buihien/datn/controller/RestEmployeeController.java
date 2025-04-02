package com.buihien.datn.controller;

import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.dto.test.EmployeeDTO;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/public/employee")
@RestController
public class RestEmployeeController extends GenericApi<EmployeeDTO, SearchDto> {

    public RestEmployeeController(GenericService<EmployeeDTO, SearchDto> genericService) {
        super(EmployeeDTO.class, genericService);
    }
}
