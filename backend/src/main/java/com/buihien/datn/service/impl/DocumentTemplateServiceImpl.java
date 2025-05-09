package com.buihien.datn.service.impl;

import com.buihien.datn.domain.DocumentItem;
import com.buihien.datn.domain.DocumentTemplate;
import com.buihien.datn.dto.DocumentItemDto;
import com.buihien.datn.dto.DocumentTemplateDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.DocumentItemRepository;
import com.buihien.datn.service.DocumentTemplateService;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class DocumentTemplateServiceImpl extends GenericServiceImpl<DocumentTemplate, DocumentTemplateDto, SearchDto> implements DocumentTemplateService {
    private final DocumentItemRepository documentItemRepository;

    public DocumentTemplateServiceImpl(DocumentItemRepository documentItemRepository) {
        super();
        this.documentItemRepository = documentItemRepository;
    }

    @Override
    protected DocumentTemplateDto convertToDto(DocumentTemplate entity) {
        return new DocumentTemplateDto(entity, true);
    }

    @Override
    protected DocumentTemplate convertToEntity(DocumentTemplateDto dto) {
        DocumentTemplate entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new DocumentTemplate();
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());

        // Set các tài liệu trong bộ hồ sơ/tài liệu
        if (entity.getDocumentItems() == null) {
            entity.setDocumentItems(new HashSet<>());
        }
        entity.getDocumentItems().clear();
        if (dto.getDocumentItems() != null && !dto.getDocumentItems().isEmpty()) {
            Set<Integer> displayOrders = new HashSet<>();
            for (DocumentItemDto item : dto.getDocumentItems()) {
                if (item.getDisplayOrder() != null) {
                    if (!displayOrders.add(item.getDisplayOrder())) {
                        throw new InvalidDataException("Thứ tự hiển thị không được trùng nhau");
                    }
                }
            }
            for (DocumentItemDto item : dto.getDocumentItems()) {
                DocumentItem documentItem = null;
                if (item.getId() != null) {
                    documentItem = documentItemRepository.findById(item.getId()).orElse(null);
                }
                if (documentItem == null) {
                    documentItem = new DocumentItem();
                }
                documentItem.setName(item.getName());
                documentItem.setDescription(item.getDescription());
                documentItem.setDisplayOrder(item.getDisplayOrder());
                documentItem.setIsRequired(item.getIsRequired());
                documentItem.setDocumentTemplate(entity);
                entity.getDocumentItems().add(documentItem);
            }
        }
        return entity;
    }

    @Override
    public Page<DocumentTemplateDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM DocumentTemplate entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.DocumentTemplateDto(entity, false) FROM DocumentTemplate entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), DocumentTemplateDto.class);
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
