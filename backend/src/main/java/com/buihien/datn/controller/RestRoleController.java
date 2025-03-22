package com.buihien.datn.controller;

import com.buihien.datn.dto.RoleDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/public/role")
@RestController
public class RestRoleController extends GenericApi<RoleDto, SearchDto> {
}
