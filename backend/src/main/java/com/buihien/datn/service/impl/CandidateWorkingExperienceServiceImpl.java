package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Candidate;
import com.buihien.datn.domain.CandidateWorkingExperience;
import com.buihien.datn.dto.BankDto;
import com.buihien.datn.dto.CandidateWorkingExperienceDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.CandidateRepository;
import com.buihien.datn.repository.CandidateWorkingExperienceRepository;
import com.buihien.datn.service.CandidateWorkingExperienceService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CandidateWorkingExperienceServiceImpl extends GenericServiceImpl<CandidateWorkingExperience, CandidateWorkingExperienceDto, SearchDto> implements CandidateWorkingExperienceService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    protected CandidateWorkingExperienceDto convertToDto(CandidateWorkingExperience entity) {
        return new CandidateWorkingExperienceDto(entity, true);
    }

    @Override
    protected CandidateWorkingExperience convertToEntity(CandidateWorkingExperienceDto dto) {
        CandidateWorkingExperience entity = null;

        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }

        if (entity == null) {
            entity = new CandidateWorkingExperience();
        }

        // Load Candidate
        Candidate candidate = null;
        if (dto.getCandidate() != null && dto.getCandidate().getId() != null) {
            candidate = candidateRepository.findById(dto.getCandidate().getId()).orElse(null);
        }
        if (candidate == null) {
            throw new ResourceNotFoundException("Ứng viên không tồn tại");
        }
        entity.setCandidate(candidate);

        entity.setCompanyName(dto.getCompanyName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setPosition(dto.getPosition());
        entity.setSalary(dto.getSalary());
        entity.setLeavingReason(dto.getLeavingReason());
        entity.setDescription(dto.getDescription());

        return entity;
    }


    @Override
    public Page<CandidateWorkingExperienceDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM CandidateWorkingExperience entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.CandidateWorkingExperienceDto(entity, false) FROM CandidateWorkingExperience entity WHERE (1=1) ");

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
            whereClause.append(" AND entity.person.id = :ownerId ");
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

        Query q = manager.createQuery(sql.toString(), BankDto.class);
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
}
