package com.buihien.datn.controller;

import com.buihien.datn.dto.EducationDegreeDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/education-degree")
@RestController
public class RestEducationDegreeController extends GenericApi<EducationDegreeDto, SearchDto> {

    public RestEducationDegreeController(GenericService<EducationDegreeDto, SearchDto> genericService) {
        super(EducationDegreeDto.class, genericService);
    }
}
