package com.buihien.datn.service;

import com.buihien.datn.domain.Candidate;
import com.buihien.datn.dto.StaffDto;
import com.buihien.datn.dto.search.StaffSearchDto;
import com.buihien.datn.generic.GenericService;

public interface StaffService extends GenericService<StaffDto, StaffSearchDto> {
    StaffDto convertCandidateToStaff(Candidate candidate);

    StaffDto getCurrentStaff();
}
