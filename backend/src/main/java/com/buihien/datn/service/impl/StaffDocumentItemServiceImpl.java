package com.buihien.datn.service.impl;

import com.buihien.datn.domain.*;
import com.buihien.datn.dto.DocumentItemDto;
import com.buihien.datn.dto.StaffDocumentItemDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.DocumentItemRepository;
import com.buihien.datn.repository.StaffDocumentItemRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.FileDescriptionService;
import com.buihien.datn.service.StaffDocumentItemService;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class StaffDocumentItemServiceImpl extends GenericServiceImpl<StaffDocumentItem, StaffDocumentItemDto, SearchDto> implements StaffDocumentItemService {
    private static final Logger logger = LoggerFactory.getLogger(StaffDocumentItemServiceImpl.class);

    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private DocumentItemRepository documentItemRepository;
    @Autowired
    private FileDescriptionService fileDescriptionService;
    @Autowired
    private StaffDocumentItemRepository staffDocumentItemRepository;

    @Override
    protected StaffDocumentItemDto convertToDto(StaffDocumentItem entity) {
        return new StaffDocumentItemDto(entity, true);
    }

    @Override
    protected StaffDocumentItem convertToEntity(StaffDocumentItemDto dto) {
        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            staff = staffRepository.findById(dto.getStaff().getId()).orElse(null);
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }
        if (staff.getDocumentTemplate() == null) {
            throw new ResourceNotFoundException("Nhân viên chưa có mẫu hồ sơ");
        }

        StaffDocumentItem entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null && staff.getDocumentTemplate() != null && staff.getDocumentTemplate().getId() != null && dto.getDocumentItem() != null && dto.getDocumentItem().getId() != null) {
            entity = staffDocumentItemRepository.findByStaffIdAndDocumentItemId(staff.getId(), dto.getDocumentItem().getId());
        }
        if (entity == null) {
            entity = new StaffDocumentItem();
        }
        entity.setStaff(staff);

        DocumentItem documentItem = null;
        if (dto.getDocumentItem() != null && dto.getDocumentItem().getId() != null) {
            documentItem = documentItemRepository.findById(dto.getDocumentItem().getId()).orElse(null);
        }
        if (documentItem == null) {
            throw new ResourceNotFoundException("Tài liệu không tồn tại");
        }
        entity.setDocumentItem(documentItem);
        FileDescription newFile = null;
        if (dto.getDocumentFile() != null && dto.getDocumentFile().getId() != null) {
            newFile = fileDescriptionService.getEntityById(dto.getDocumentFile().getId());
        }

        FileDescription oldFile = entity.getDocumentFile();

        if (oldFile != null && oldFile.getId() != null) {
            // Nếu file mới khác file cũ thì xóa file cũ
            if (newFile == null || !oldFile.getId().equals(newFile.getId())) {
                fileDescriptionService.deleteById(oldFile.getId());
            }
        }
        entity.setDocumentFile(newFile);
        entity.setSubmitDate(entity.getSubmitDate());
        return entity;
    }

    @Override
    public Page<StaffDocumentItemDto> pagingSearch(SearchDto dto) {
        return new PageImpl<>(null);
    }

    @Override
    public void handleSetUpStaffDocumentItemByDocumentTemplate(DocumentTemplate documentTemplate, Staff staff) {
        if (documentTemplate == null) {
            throw new ResourceNotFoundException("Mẫu hồ sơ không tồn tại");
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }

        if (staff.getDocumentTemplate() != null
                && staff.getDocumentTemplate().getId().equals(documentTemplate.getId())
                && staff.getStaffDocumentItems() != null
                && !staff.getStaffDocumentItems().isEmpty()) {
            return;
        }


        if (documentTemplate.getDocumentItems() != null && !documentTemplate.getDocumentItems().isEmpty()) {
            if (staff.getStaffDocumentItems() == null) {
                staff.setStaffDocumentItems(new HashSet<>());
            }
            // Xóa các tài liệu cũ của nhân viên
            staff.getStaffDocumentItems().clear();
            // Lưu lại các tài liệu mới
            for (DocumentItem documentItem : documentTemplate.getDocumentItems()) {
                StaffDocumentItem staffDocumentItem = staffDocumentItemRepository.findByStaffIdAndDocumentItemId(staff.getId(), documentItem.getId());
                if (staffDocumentItem == null) {
                    staffDocumentItem = new StaffDocumentItem();
                }
                staffDocumentItem.setDocumentItem(documentItem);
                staffDocumentItem.setStaff(staff);
                staff.getStaffDocumentItems().add(staffDocumentItem);
            }
        }
    }

    @Override
    public List<StaffDocumentItemDto> getStaffDocumentItemByDocumentTemplate(UUID staffId) {
        Staff staff = staffRepository.findById(staffId).orElse(null);
        if (staff == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }

        if (staff.getDocumentTemplate() == null) {
            return List.of();
        }

        List<StaffDocumentItemDto> response = new ArrayList<>();

        for (DocumentItem item : staff.getDocumentTemplate().getDocumentItems()) {
            StaffDocumentItem staffDocumentItem = null;
            if (item.getId() != null) {
                staffDocumentItem = staffDocumentItemRepository.findByStaffIdAndDocumentItemId(staff.getId(), item.getId());
            }
            if (staffDocumentItem == null) {
                staffDocumentItem = new StaffDocumentItem();
            }

            staffDocumentItem.setDocumentItem(item);
            staffDocumentItem.setStaff(staff);

            response.add(new StaffDocumentItemDto(staffDocumentItem, true));
        }

        response.sort(Comparator.comparing(dto -> {
            DocumentItemDto documentItem = dto.getDocumentItem();
            return documentItem != null && documentItem.getDisplayOrder() != null ? documentItem.getDisplayOrder() : 0;
        }));

        return response;
    }
}
