package com.buihien.datn.service;

import com.buihien.datn.domain.DocumentTemplate;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.dto.StaffDocumentItemDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericService;

public interface StaffDocumentItemService extends GenericService<StaffDocumentItemDto, SearchDto> {
    void handleSetUpStaffDocumentItemByDocumentTemplate(DocumentTemplate documentTemplate, Staff staff);
}
