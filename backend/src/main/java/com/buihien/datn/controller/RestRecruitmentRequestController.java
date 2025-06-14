package com.buihien.datn.controller;

import com.buihien.datn.dto.RecruitmentRequestDto;
import com.buihien.datn.dto.search.RecruitmentRequestSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/recruitment-request")
@RestController
public class RestRecruitmentRequestController extends GenericApi<RecruitmentRequestDto, RecruitmentRequestSearchDto> {

    public RestRecruitmentRequestController(GenericService<RecruitmentRequestDto, RecruitmentRequestSearchDto> genericService) {
        super(RecruitmentRequestDto.class, genericService);
    }
}

