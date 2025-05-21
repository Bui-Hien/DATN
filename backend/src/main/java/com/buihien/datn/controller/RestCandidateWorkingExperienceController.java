package com.buihien.datn.controller;

import com.buihien.datn.dto.CandidateWorkingExperienceDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/candidate-working-experience")
@RestController
public class RestCandidateWorkingExperienceController extends GenericApi<CandidateWorkingExperienceDto, SearchDto> {

    public RestCandidateWorkingExperienceController(GenericService<CandidateWorkingExperienceDto, SearchDto> genericService) {
        super(CandidateWorkingExperienceDto.class, genericService);
    }
}

