package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.CandidateDto;
import com.buihien.datn.dto.candidateupdatestatus.CandidateStatusDto;
import com.buihien.datn.dto.search.CandidateSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/candidate")
@RestController
public class RestCandidateController extends GenericApi<CandidateDto, CandidateSearchDto> {

    public RestCandidateController(GenericService<CandidateDto, CandidateSearchDto> genericService) {
        super(CandidateDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/update-status")
    public ResponseData<Integer> updateStatus(@Valid @RequestBody CandidateStatusDto dto) {
        Integer result = ((CandidateService) genericService).updateStatus(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/pre-screened")
    public ResponseData<Integer> preScreened(@Valid @RequestBody CandidateStatusDto dto) {
        Integer result = ((CandidateService) genericService).preScreened(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
    }
}

