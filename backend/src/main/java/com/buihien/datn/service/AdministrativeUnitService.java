package com.buihien.datn.service;

import com.buihien.datn.dto.AdministrativeUnitDto;
import com.buihien.datn.dto.search.AdministrativeUnitSearchDto;
import com.buihien.datn.generic.GenericService;
import org.springframework.data.domain.Page;

public interface AdministrativeUnitService extends GenericService<AdministrativeUnitDto, AdministrativeUnitSearchDto> {
    Page<AdministrativeUnitDto> pagingTreeSearch(AdministrativeUnitSearchDto dto);
}
