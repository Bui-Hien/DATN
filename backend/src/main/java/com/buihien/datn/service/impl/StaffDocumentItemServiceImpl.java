package com.buihien.datn.service.impl;

import com.buihien.datn.domain.*;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
        StaffDocumentItem entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new StaffDocumentItem();
        }
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
        entity.setStaff(staff);

        DocumentItem documentItem = null;
        if (dto.getDocumentItem() != null && dto.getDocumentItem().getId() != null) {
            documentItem = documentItemRepository.findById(dto.getDocumentItem().getId()).orElse(null);
        }
        if (documentItem == null) {
            throw new ResourceNotFoundException("Tài liệu không tồn tại");
        }
        entity.setDocumentItem(documentItem);

        if (staff.getDocumentTemplate() != null && !staff.getStaffDocumentItems().isEmpty()) {
            boolean found = false;
            Set<DocumentItem> documentItems = staff.getDocumentTemplate().getDocumentItems();
            if (documentItems != null) {
                for (DocumentItem item : documentItems) {
                    if (item.getId().equals(documentItem.getId())) {
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                throw new InvalidDataException("Tài liệu không thuộc mẫu hồ sơ của nhân viên.");
            }
        }
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
    public Boolean deleteById(UUID id) {
        logger.info("Đang xóa bản ghi với ID: {}", id);
        StaffDocumentItem entity = repository.findById(id).orElse(null);
        if (entity != null) {
            if (entity.getDocumentFile() != null && entity.getDocumentFile().getId() != null) {
                // Xóa file đính kèm nếu có
                fileDescriptionService.deleteById(entity.getDocumentFile().getId());
            }
            // Xóa entity
            repository.deleteById(id);
            logger.info("Xóa bản ghi thành công với ID: {}", id);
            return true;
        } else {
            logger.warn("Không tìm thấy bản ghi để xóa với ID: {}", id);
            throw new ResourceNotFoundException("Không tìm thấy bản ghi để xóa với ID: " + id);
        }
    }


    @Override
    public int deleteMultiple(List<UUID> ids) {
        logger.info("Đang xóa nhiều bản ghi với các ID: {}", ids);
        if (ids == null || ids.isEmpty()) {
            logger.warn("Danh sách ID rỗng. Không có bản ghi nào để xóa.");
            throw new ResourceNotFoundException("Danh sách ID rỗng. Không có bản ghi nào để xóa.");
        }

        for (UUID id : ids) {
            StaffDocumentItem entity = repository.findById(id).orElse(null);
            if (entity != null && entity.getDocumentFile() != null && entity.getDocumentFile().getId() != null) {
                // Xóa file đính kèm nếu có
                fileDescriptionService.deleteById(entity.getDocumentFile().getId());
            }
        }

        repository.deleteAllById(ids);
        logger.info("Đã xóa thành công {} bản ghi", ids.size());
        return ids.size();
    }


    @Override
    public Page<StaffDocumentItemDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM StaffDocumentItem entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.StaffDocumentItemDto(entity, true) FROM StaffDocumentItem entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
        }

        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.createdAt >= :fromDate ");
        }
        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.createdAt <= :toDate ");
        }

        sql.append(whereClause);
        sqlCount.append(whereClause);

        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.createdAt ASC" : " ORDER BY entity.createdAt DESC");

        Query q = manager.createQuery(sql.toString(), StaffDocumentItemDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }

        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", dto.getFromDate());
            qCount.setParameter("fromDate", dto.getFromDate());
        }
        if (dto.getToDate() != null) {
            q.setParameter("toDate", dto.getToDate());
            qCount.setParameter("toDate", dto.getToDate());
        }
        if (!isExportExcel) {
            q.setFirstResult(pageIndex * pageSize);
            q.setMaxResults(pageSize);

            return new PageImpl<>(q.getResultList(), PageRequest.of(pageIndex, pageSize), (long) qCount.getSingleResult());
        }
        return new PageImpl<>(q.getResultList());
    }

    @Override
    public void handleSetUpStaffDocumentItemByDocumentTemplate(DocumentTemplate documentTemplate, Staff staff) {
        if (documentTemplate == null) {
            throw new ResourceNotFoundException("Mẫu hồ sơ không tồn tại");
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
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
                staff.getStaffDocumentItems().add(staffDocumentItem);
            }
        }
    }
}
