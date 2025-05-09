package com.buihien.datn.service.impl;

import com.buihien.datn.domain.SalaryTemplate;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.domain.StaffSalaryTemplateHistory;
import com.buihien.datn.dto.StaffSalaryTemplateHistoryDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.SalaryTemplateRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.repository.StaffSalaryTemplateHistoryRepository;
import com.buihien.datn.service.StaffSalaryTemplateHistoryService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class StaffSalaryTemplateHistoryServiceImpl extends GenericServiceImpl<StaffSalaryTemplateHistory, StaffSalaryTemplateHistoryDto, SearchDto> implements StaffSalaryTemplateHistoryService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private SalaryTemplateRepository salaryTemplateRepository;
    @Autowired
    private StaffSalaryTemplateHistoryRepository staffSalaryTemplateHistoryRepository;

    @Override
    protected StaffSalaryTemplateHistoryDto convertToDto(StaffSalaryTemplateHistory entity) {
        return new StaffSalaryTemplateHistoryDto(entity);
    }

    @Override
    protected StaffSalaryTemplateHistory convertToEntity(StaffSalaryTemplateHistoryDto dto) {
        StaffSalaryTemplateHistory entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new StaffSalaryTemplateHistory();
        }
        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            staff = staffRepository.findById(dto.getStaff().getId()).orElse(null);
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Không tìm thấy nhân viên");
        }
        entity.setStaff(staff);

        SalaryTemplate salaryTemplate = null;
        if (dto.getSalaryTemplate() != null && dto.getSalaryTemplate().getId() != null) {
            salaryTemplate = salaryTemplateRepository.findById(dto.getSalaryTemplate().getId()).orElse(null);
        }
        if (salaryTemplate == null) {
            throw new ResourceNotFoundException("Không tìm thấy mẫu bảng lương");
        }
        entity.setSalaryTemplate(salaryTemplate);
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());

        if (Boolean.TRUE.equals(dto.getIsCurrent())) {
            entity.setIsCurrent(true);
            this.updateOtherTemplatesToInactive(entity, staff.getId());
        } else {
            entity.setIsCurrent(false);
        }
        return entity;
    }

    @Override
    public Page<StaffSalaryTemplateHistoryDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM StaffSalaryTemplateHistory entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.StaffSalaryTemplateHistoryDto(entity) FROM StaffSalaryTemplateHistory entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
        }
        if (dto.getOwnerId() != null) {
            whereClause.append(" AND entity.staff.id = :ownerId ");
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

        Query q = manager.createQuery(sql.toString(), StaffSalaryTemplateHistoryDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (dto.getOwnerId() != null) {
            q.setParameter("ownerId", dto.getOwnerId());
            qCount.setParameter("ownerId", dto.getOwnerId());
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

    private void updateOtherTemplatesToInactive(StaffSalaryTemplateHistory currentEntity, UUID staffId) {
        List<StaffSalaryTemplateHistory> histories = staffSalaryTemplateHistoryRepository.findByStaffId(staffId);
        boolean changed = false;
        for (StaffSalaryTemplateHistory history : histories) {
            if (!history.getId().equals(currentEntity.getId()) && Boolean.TRUE.equals(history.getIsCurrent())) {
                history.setIsCurrent(false);
                changed = true;
            }
        }
        if (changed) {
            staffSalaryTemplateHistoryRepository.saveAll(histories);
        }
    }
}
