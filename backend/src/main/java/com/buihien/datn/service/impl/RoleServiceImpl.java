package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Role;
import com.buihien.datn.dto.RoleDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.service.RoleService;
import com.buihien.datn.util.DateTimeUtil;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, RoleDto, SearchDto> implements RoleService {
    @Override
    protected RoleDto convertToDto(Role entity) {
        return new RoleDto(entity);
    }

    @Override
    protected Role convertToEntity(RoleDto dto) {
        Role entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new Role();
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    @Override
    public Page<RoleDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getIsExportExcel() != null && dto.getIsExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Role entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new RoleDto(entity) FROM Role entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.description) LIKE LOWER(:text)) ");
        }

        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.createDate >= :fromDate ");
        }
        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.createDate <= :toDate ");
        }
        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.createdAt ASC" : " ORDER BY entity.createdAt DESC");

        sql.append(whereClause);
        sqlCount.append(whereClause);

        Query q = manager.createQuery(sql.toString(), RoleDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }

        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", DateTimeUtil.getStartOfDay(dto.getFromDate()));
            qCount.setParameter("fromDate", DateTimeUtil.getStartOfDay(dto.getFromDate()));
        }
        if (dto.getToDate() != null) {
            q.setParameter("toDate", DateTimeUtil.getEndOfDay(dto.getToDate()));
            qCount.setParameter("toDate", DateTimeUtil.getEndOfDay(dto.getToDate()));
        }
        if (!isExportExcel) {
            q.setFirstResult(pageIndex * pageSize);
            q.setMaxResults(pageSize);

            return new PageImpl<>(q.getResultList(), PageRequest.of(pageIndex, pageSize), (long) qCount.getSingleResult());
        }
        return new PageImpl<>(q.getResultList());
    }
}
