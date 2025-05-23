package com.buihien.datn.service;

import com.buihien.datn.dto.SalaryResultDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericService;

import java.util.UUID;

public interface SalaryResultService extends GenericService<SalaryResultDto, SearchDto> {
    Boolean getRecalculateSalary(UUID salaryResultId);
}
