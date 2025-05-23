package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.AdministrativeUnitDto;
import com.buihien.datn.dto.search.AdministrativeUnitSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.AdministrativeUnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/administrative-unit")
@RestController
public class RestAdministrativeUnitController extends GenericApi<AdministrativeUnitDto, AdministrativeUnitSearchDto> {

    public RestAdministrativeUnitController(GenericService<AdministrativeUnitDto, AdministrativeUnitSearchDto> genericService) {
        super(AdministrativeUnitDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_ADMIN})
    @PostMapping("/paging-tree-search")
    public ResponseData<Page<AdministrativeUnitDto>> pagingTreeSearch(@Valid @RequestBody AdministrativeUnitSearchDto dto) {
        Page<AdministrativeUnitDto> result = ((AdministrativeUnitService) genericService).pagingTreeSearch(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }
}
