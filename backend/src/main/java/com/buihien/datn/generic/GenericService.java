package com.buihien.datn.generic;

import com.buihien.datn.dto.AuditableDto;
import com.buihien.datn.dto.search.SearchDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface GenericService<DTO extends AuditableDto, S extends SearchDto> {
    Boolean deleteById(UUID id);

    int deleteMultiple(List<UUID> ids);

    DTO saveOrUpdate(DTO dto);

    Integer saveOrUpdateList(List<DTO> dtos);

    Page<DTO> paging(int pageIndex, int pageSize);

    Page<DTO> pagingSearch(S search);

    DTO getById(UUID id);
}
