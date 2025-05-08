package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Position;
import com.buihien.datn.domain.RecruitmentRequest;
import com.buihien.datn.domain.RecruitmentRequestItem;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.dto.RecruitmentRequestDto;
import com.buihien.datn.dto.RecruitmentRequestItemDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.PositionRepository;
import com.buihien.datn.repository.RecruitmentRequestItemRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.RecruitmentRequestService;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;

@Service
public class RecruitmentRequestServiceImpl extends GenericServiceImpl<RecruitmentRequest, RecruitmentRequestDto, SearchDto> implements RecruitmentRequestService {
    private final StaffRepository staffRepository;
    private final RecruitmentRequestItemRepository recruitmentRequestItemRepository;
    private final PositionRepository positionRepository;

    public RecruitmentRequestServiceImpl(StaffRepository staffRepository, RecruitmentRequestItemRepository recruitmentRequestItemRepository, PositionRepository positionRepository) {
        super();
        this.staffRepository = staffRepository;
        this.recruitmentRequestItemRepository = recruitmentRequestItemRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    protected RecruitmentRequestDto convertToDto(RecruitmentRequest entity) {
        return new RecruitmentRequestDto(entity, true);
    }

    @Override
    protected RecruitmentRequest convertToEntity(RecruitmentRequestDto dto) {
        RecruitmentRequest entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new RecruitmentRequest();
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());

        entity.setRecruitmentRequestStatus(dto.getRecruitmentRequestStatus());
        Staff proposer = null;
        if (dto.getProposer() != null && dto.getProposer().getId() != null) {
            proposer = staffRepository.findById(dto.getProposer().getId()).orElse(null);
        }
        entity.setProposer(proposer);
        entity.setProposalDate(dto.getProposalDate());

        //Duyệt yêu cầu là 1 api riêng tự động lấy người duyệt và phải đủ quyền
        //Thới gian duyệt cũng tự động lấy

        if (entity.getRecruitmentRequestItems() == null) {
            entity.setRecruitmentRequestItems(new HashSet<>());
        }
        entity.getRecruitmentRequestItems().clear();
        if (dto.getRecruitmentRequestItems() != null && !dto.getRecruitmentRequestItems().isEmpty()) {
            for (RecruitmentRequestItemDto item : dto.getRecruitmentRequestItems()) {
                RecruitmentRequestItem recruitmentRequestItem = null;
                if (item.getId() != null) {
                    recruitmentRequestItem = recruitmentRequestItemRepository.findById(item.getId()).orElse(null);
                }
                if (recruitmentRequestItem == null) {
                    recruitmentRequestItem = new RecruitmentRequestItem();
                }
                recruitmentRequestItem.setRecruitmentRequest(entity);

                Position position = null;
                if (item.getPosition() != null && item.getPosition().getId() != null) {
                    position = positionRepository.findById(item.getPosition().getId()).orElse(null);
                }
                recruitmentRequestItem.setPosition(position);
                recruitmentRequestItem.setGender(item.getGender());
                recruitmentRequestItem.setYearOfExperience(item.getYearOfExperience());
                recruitmentRequestItem.setMinimumAge(item.getMinimumAge());
                recruitmentRequestItem.setMaximumAge(item.getMaximumAge());
                recruitmentRequestItem.setMinimumIncome(item.getMinimumIncome());
                recruitmentRequestItem.setMaximumIncome(item.getMaximumIncome());
                recruitmentRequestItem.setDescription(item.getDescription());
                recruitmentRequestItem.setRequest(item.getRequest());
            }
        }
        return entity;
    }

    @Override
    public Page<RecruitmentRequestDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM RecruitmentRequest entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.RecruitmentRequestDto(entity, false) FROM RecruitmentRequest entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), RecruitmentRequestDto.class);
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
