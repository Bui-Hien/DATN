package com.buihien.datn.service;

import com.buihien.datn.domain.*;
import com.buihien.datn.dto.SalaryResultItemDetailDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericService;

public interface SalaryResultItemDetailService extends GenericService<SalaryResultItemDetailDto, SearchDto> {
    void handleSetUpSalaryResultItemDetailByStaff(SalaryResultItem salaryResultItem, SalaryTemplate salaryTemplate);
}
