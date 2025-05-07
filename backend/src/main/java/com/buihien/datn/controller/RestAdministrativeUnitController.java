package com.buihien.datn.controller;

import com.buihien.datn.dto.AdministrativeUnitDto;
import com.buihien.datn.dto.search.AdministrativeUnitSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/administrative-unit")
@RestController
public class RestAdministrativeUnitController extends GenericApi<AdministrativeUnitDto, AdministrativeUnitSearchDto> {

    public RestAdministrativeUnitController(GenericService<AdministrativeUnitDto, AdministrativeUnitSearchDto> genericService) {
        super(AdministrativeUnitDto.class, genericService);
    }
}
