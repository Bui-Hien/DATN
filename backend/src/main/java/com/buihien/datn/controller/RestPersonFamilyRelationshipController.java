package com.buihien.datn.controller;

import com.buihien.datn.dto.PersonFamilyRelationshipDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/person-family-relationship")
@RestController
public class RestPersonFamilyRelationshipController extends GenericApi<PersonFamilyRelationshipDto, SearchDto> {

    public RestPersonFamilyRelationshipController(GenericService<PersonFamilyRelationshipDto, SearchDto> genericService) {
        super(PersonFamilyRelationshipDto.class, genericService);
    }
}
