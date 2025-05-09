package com.buihien.datn.service.impl;

import com.buihien.datn.domain.SalaryPeriod;
import com.buihien.datn.domain.SalaryResult;
import com.buihien.datn.domain.SalaryResultItem;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.dto.SalaryResultDto;
import com.buihien.datn.dto.SalaryResultItemDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.SalaryPeriodRepository;
import com.buihien.datn.repository.SalaryResultItemRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.SalaryResultService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;

@Service
public class SalaryResultServiceImpl extends GenericServiceImpl<SalaryResult, SalaryResultDto, SearchDto> implements SalaryResultService {
    @Autowired
    private SalaryPeriodRepository salaryPeriodRepository;
    @Autowired
    private SalaryResultItemRepository salaryResultItemRepository;
    @Autowired
    private StaffRepository staffRepository;

    @Override
    protected SalaryResultDto convertToDto(SalaryResult entity) {
        return new SalaryResultDto(entity, true);
    }

    @Override
    protected SalaryResult convertToEntity(SalaryResultDto dto) {
        SalaryResult entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new SalaryResult();
        }
        entity.setName(dto.getName());
        SalaryPeriod salaryPeriod = null;
        if (dto.getSalaryPeriod() != null && dto.getSalaryPeriod().getId() != null) {
            salaryPeriod = salaryPeriodRepository.findById(dto.getSalaryPeriod().getId()).orElse(null);
        }
        if (salaryPeriod == null) {
            throw new ResourceNotFoundException("Kỳ lương không tồn tại");
        }
        entity.setSalaryPeriod(salaryPeriod);
        if (entity.getSalaryResultItems() == null) {
            entity.setSalaryResultItems(new HashSet<>());
        }
        if (dto.getSalaryResultItems() != null && !dto.getSalaryResultItems().isEmpty()) {
            entity.getSalaryResultItems().clear();
            for (SalaryResultItemDto item : dto.getSalaryResultItems()) {
                SalaryResultItem salaryResultItem = null;
                if (item.getId() != null) {
                    salaryResultItem = salaryResultItemRepository.findById(item.getId()).orElse(null);
                }
                if (salaryResultItem == null) {
                    salaryResultItem = new SalaryResultItem();
                }
                salaryResultItem.setSalaryResult(entity);
                Staff staff = null;
                if (item.getStaff() != null && item.getStaff().getId() != null) {
                    staff = staffRepository.findById(item.getStaff().getId()).orElse(null);
                }
                if (staff == null) {
                    throw new ResourceNotFoundException("Nhân viên không tồn tại");
                }
            }
        }
        return entity;
    }

    @Override
    public Page<SalaryResultDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM SalaryResult entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.SalaryResultDto(entity, false) FROM SalaryResult entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), SalaryResultDto.class);
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
}
