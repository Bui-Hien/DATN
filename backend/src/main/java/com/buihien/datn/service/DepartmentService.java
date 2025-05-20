package com.buihien.datn.service;

import com.buihien.datn.dto.DepartmentDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface DepartmentService extends GenericService<DepartmentDto, SearchDto> {
    Page<DepartmentDto> pagingTreeSearch(SearchDto dto);
}
