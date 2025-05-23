package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.SalaryPeriod;
import com.buihien.datn.dto.SalaryPeriodDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.service.SalaryPeriodService;
import com.buihien.datn.util.DateTimeUtil;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.buihien.datn.util.DateTimeUtil.calculateDaysBetween;

@Service
public class SalaryPeriodServiceImpl extends GenericServiceImpl<SalaryPeriod, SalaryPeriodDto, SearchDto> implements SalaryPeriodService {
    @Override
    protected SalaryPeriodDto convertToDto(SalaryPeriod entity) {
        return new SalaryPeriodDto(entity);
    }

    @Override
    protected SalaryPeriod convertToEntity(SalaryPeriodDto dto) {
        SalaryPeriod entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new SalaryPeriod();
            entity.setSalaryPeriodStatus(DatnConstants.SalaryPeriodStatus.DRAFT.getValue());
        }
        if (DatnConstants.SalaryPeriodStatus.DRAFT.getValue().equals(dto.getSalaryPeriodStatus())) {
            entity.setName(dto.getName());
            entity.setCode(dto.getCode());
            entity.setDescription(dto.getDescription());
            entity.setStartDate(dto.getStartDate());
            entity.setEndDate(dto.getEndDate());
            if (dto.getStartDate() != null && dto.getEndDate() != null) {
                long betweenDate = calculateDaysBetween(dto.getStartDate(), dto.getEndDate());
                if (dto.getEstimatedWorkingDays() != null && dto.getEstimatedWorkingDays() > betweenDate) {
                    throw new InvalidDataException("Số ngày làm việc trong kỳ lương không thể lớn hơn tổng số ngày thực tế");
                }
            }
            entity.setEstimatedWorkingDays(dto.getEstimatedWorkingDays());
        }

        return entity;
    }

    @Override
    public Page<SalaryPeriodDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM SalaryPeriod entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.SalaryPeriodDto(entity) FROM SalaryPeriod entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), SalaryPeriodDto.class);
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
