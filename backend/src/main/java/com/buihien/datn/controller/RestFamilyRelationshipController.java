package com.buihien.datn.controller;

import com.buihien.datn.dto.FamilyRelationshipDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/family-relationship")
@RestController
public class RestFamilyRelationshipController extends GenericApi<FamilyRelationshipDto, SearchDto> {

    public RestFamilyRelationshipController(GenericService<FamilyRelationshipDto, SearchDto> genericService) {
        super(FamilyRelationshipDto.class, genericService);
    }
}
