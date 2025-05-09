package com.buihien.datn.service.impl;

import com.buihien.datn.domain.SalaryTemplate;
import com.buihien.datn.domain.SalaryTemplateItem;
import com.buihien.datn.dto.SalaryTemplateDto;
import com.buihien.datn.dto.SalaryTemplateItemDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.SalaryTemplateItemRepository;
import com.buihien.datn.service.SalaryTemplateService;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;

@Service
public class SalaryTemplateServiceImpl extends GenericServiceImpl<SalaryTemplate, SalaryTemplateDto, SearchDto> implements SalaryTemplateService {
    private final SalaryTemplateItemRepository salaryTemplateItemRepository;

    public SalaryTemplateServiceImpl(SalaryTemplateItemRepository salaryTemplateItemRepository) {
        super();
        this.salaryTemplateItemRepository = salaryTemplateItemRepository;
    }

    @Override
    protected SalaryTemplateDto convertToDto(SalaryTemplate entity) {
        return new SalaryTemplateDto(entity, false);
    }

    @Override
    protected SalaryTemplate convertToEntity(SalaryTemplateDto dto) {
        SalaryTemplate entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new SalaryTemplate();
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());

        if (entity.getTemplateItems() == null) {
            entity.setTemplateItems(new HashSet<>());
        }
        entity.getTemplateItems().clear();
        if (dto.getTemplateItems() != null && !dto.getTemplateItems().isEmpty()) {
            for (SalaryTemplateItemDto item : dto.getTemplateItems()) {
                SalaryTemplateItem salaryTemplateItem = null;
                if (item.getId() != null) {
                    salaryTemplateItem = salaryTemplateItemRepository.findById(item.getId()).orElse(null);
                }
                if (salaryTemplateItem == null) {
                    salaryTemplateItem = new SalaryTemplateItem();
                }
                salaryTemplateItem.setDisplayOrder(item.getDisplayOrder());
                salaryTemplateItem.setSalaryTemplate(entity);
                salaryTemplateItem.setSalaryItemType(item.getSalaryItemType());
                salaryTemplateItem.setDefaultAmount(item.getDefaultAmount());
                salaryTemplateItem.setFormula(item.getFormula());
            }
        }
        return entity;
    }

    @Override
    public Page<SalaryTemplateDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM SalaryTemplate entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.SalaryTemplateDto(entity, false) FROM SalaryTemplate entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), SalaryTemplateDto.class);
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
