package com.buihien.datn.service.impl;

import com.buihien.datn.domain.LogMessageQueue;
import com.buihien.datn.dto.LogMessageQueueDto;
import com.buihien.datn.dto.search.LogMessageQueueSearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.service.LogMessageQueueService;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LogMessageQueueServiceImpl extends GenericServiceImpl<LogMessageQueue, LogMessageQueueDto, LogMessageQueueSearchDto> implements LogMessageQueueService {
    @Override
    protected LogMessageQueueDto convertToDto(LogMessageQueue entity) {
        return new LogMessageQueueDto(entity);
    }

    @Override
    protected LogMessageQueue convertToEntity(LogMessageQueueDto dto) {
        if (dto == null) return null;
        LogMessageQueue entity = new LogMessageQueue();
        entity.setMessage(dto.getMessage());
        entity.setAction(dto.getAction());
        entity.setStatus(dto.getStatus());
        entity.setType(dto.getType());
        return entity;
    }

    @Override
    public Page<LogMessageQueueDto> pagingSearch(LogMessageQueueSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM LogMessageQueue entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.LogMessageQueueDto(entity) FROM LogMessageQueue entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.message) LIKE LOWER(:text) OR LOWER(entity.action) LIKE LOWER(:text)) ");
        }

        if (dto.getStatus() != null) {
            whereClause.append(" AND entity.status = :status ");
        }
        if (dto.getType() != null) {
            whereClause.append(" AND entity.type = :type ");
        }

        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.createDate >= :fromDate ");
        }
        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.createDate <= :toDate ");
        }

        sql.append(whereClause);
        sqlCount.append(whereClause);

        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.createdAt ASC" : " ORDER BY entity.createdAt DESC");

        Query q = manager.createQuery(sql.toString(), LogMessageQueueDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (dto.getStatus() != null) {
            q.setParameter("status", dto.getStatus());
            qCount.setParameter("status", dto.getStatus());
        }
        if (dto.getType() != null) {
            q.setParameter("type", dto.getType());
            qCount.setParameter("type", dto.getType());
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
