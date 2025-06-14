package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.DepartmentDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/department")
@RestController
public class RestDepartmentController extends GenericApi<DepartmentDto, SearchDto> {

    public RestDepartmentController(GenericService<DepartmentDto, SearchDto> genericService) {
        super(DepartmentDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/paging-tree-search")
    public ResponseData<Page<DepartmentDto>> pagingTreeSearch(@Valid @RequestBody SearchDto dto) {
        Page<DepartmentDto> result = ((DepartmentService) genericService).pagingTreeSearch(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }

}

