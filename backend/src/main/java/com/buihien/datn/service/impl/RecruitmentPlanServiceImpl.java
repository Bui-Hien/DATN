package com.buihien.datn.service.impl;

import com.buihien.datn.domain.RecruitmentPlan;
import com.buihien.datn.domain.RecruitmentRequest;
import com.buihien.datn.dto.RecruitmentPlanDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.RecruitmentRequestRepository;
import com.buihien.datn.service.RecruitmentPlanService;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RecruitmentPlanServiceImpl extends GenericServiceImpl<RecruitmentPlan, RecruitmentPlanDto, SearchDto> implements RecruitmentPlanService {
    private final RecruitmentRequestRepository recruitmentRequestRepository;

    public RecruitmentPlanServiceImpl(RecruitmentRequestRepository recruitmentRequestRepository) {
        super();
        this.recruitmentRequestRepository = recruitmentRequestRepository;
    }

    @Override
    protected RecruitmentPlanDto convertToDto(RecruitmentPlan entity) {
        return new RecruitmentPlanDto(entity, true);
    }

    @Override
    protected RecruitmentPlan convertToEntity(RecruitmentPlanDto dto) {
        RecruitmentPlan entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new RecruitmentPlan();
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setEstimatedTimeFrom(dto.getEstimatedTimeFrom());
        entity.setEstimatedTimeTo(dto.getEstimatedTimeTo());

        RecruitmentRequest recruitmentRequest = null;
        if (dto.getRecruitmentRequest() != null && dto.getRecruitmentRequest().getId() != null) {
            recruitmentRequest = recruitmentRequestRepository.findById(dto.getRecruitmentRequest().getId()).orElse(null);
        }
        entity.setRecruitmentRequest(recruitmentRequest);
        return entity;
    }

    @Override
    public Page<RecruitmentPlanDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM RecruitmentPlan entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.RecruitmentPlanDto(entity, false) FROM RecruitmentPlan entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), RecruitmentPlanDto.class);
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
