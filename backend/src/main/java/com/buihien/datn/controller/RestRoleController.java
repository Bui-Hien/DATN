package com.buihien.datn.controller;

import com.buihien.datn.dto.RoleDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/role")
@RestController
public class RestRoleController extends GenericApi<RoleDto, SearchDto> {

    public RestRoleController(GenericService<RoleDto, SearchDto> genericService) {
        super(RoleDto.class, genericService);
    }
}
