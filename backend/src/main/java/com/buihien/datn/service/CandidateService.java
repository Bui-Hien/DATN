package com.buihien.datn.service;

import com.buihien.datn.dto.CandidateDto;
import com.buihien.datn.dto.candidateupdatestatus.CandidateStatusDto;
import com.buihien.datn.dto.candidateupdatestatus.CandidateStatusItemDto;
import com.buihien.datn.dto.search.CandidateSearchDto;
import com.buihien.datn.generic.GenericService;
import jakarta.validation.Valid;

import java.util.List;

public interface CandidateService extends GenericService<CandidateDto, CandidateSearchDto> {
    Integer updateStatus(CandidateStatusDto status);

    Integer preScreened(CandidateStatusDto dto);
}
