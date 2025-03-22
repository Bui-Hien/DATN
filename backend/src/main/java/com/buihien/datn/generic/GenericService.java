package com.buihien.datn.generic;

import com.buihien.datn.dto.AuditableEntityDto;
import com.buihien.datn.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenericService<DTO extends AuditableEntityDto, S extends SearchDto> {
    Boolean deleteById(Long id);

    int deleteMultiple(List<Long> ids);

    DTO saveOrUpdate(DTO dto);

    Page<DTO> paging(int pageIndex, int pageSize);

    Page<DTO> pagingSearch(S search);

    DTO getById(Long id);
}
