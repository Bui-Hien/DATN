package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.domain.StaffLabourAgreement;
import com.buihien.datn.dto.StaffLabourAgreementDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.StaffLabourAgreementRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.StaffLabourAgreementService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class StaffLabourAgreementServiceImpl extends GenericServiceImpl<StaffLabourAgreement, StaffLabourAgreementDto, SearchDto> implements StaffLabourAgreementService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffLabourAgreementRepository staffLabourAgreementRepository;

    protected StaffLabourAgreementDto convertToDto(StaffLabourAgreement entity) {
        return new StaffLabourAgreementDto(entity, true);
    }

    @Override
    protected StaffLabourAgreement convertToEntity(StaffLabourAgreementDto dto) {
        StaffLabourAgreement entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new StaffLabourAgreement();
        }
        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            staff = staffRepository.findById(dto.getStaff().getId()).orElse(null);
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }

        entity.setStaff(staff);
        entity.setContractType(dto.getContractType());
        entity.setLabourAgreementNumber(dto.getLabourAgreementNumber());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDurationMonths(dto.getDurationMonths());
        entity.setWorkingHour(dto.getWorkingHour());
        entity.setWorkingHourWeekMin(dto.getWorkingHourWeekMin());
        entity.setSalary(dto.getSalary());
        entity.setSignedDate(dto.getSignedDate());
        entity.setAgreementStatus(dto.getAgreementStatus());

        if (dto.getAgreementStatus() != null && dto.getAgreementStatus().equals(DatnConstants.StaffLabourAgreementStatus.SIGNED.getValue())) {
            // Lấy các hợp đồng đã được ký và chuyển trạng thái
            List<StaffLabourAgreement> signedAgreements =
                    staffLabourAgreementRepository.findByStaffIdAndAgreementStatus(staff.getId(), DatnConstants.StaffLabourAgreementStatus.SIGNED.getValue());
            Date now = new Date();

            for (StaffLabourAgreement agreement : signedAgreements) {
                if (agreement.getEndDate() != null && agreement.getEndDate().before(now)) {
                    agreement.setAgreementStatus(DatnConstants.StaffLabourAgreementStatus.EXPIRED.getValue());
                } else {
                    agreement.setAgreementStatus(DatnConstants.StaffLabourAgreementStatus.TERMINATED.getValue());
                }
            }

            // Lưu lại các thay đổi
            staffLabourAgreementRepository.saveAll(signedAgreements);

            //set hợp đồng mới về trạng đã ký
            entity.setAgreementStatus(DatnConstants.StaffLabourAgreementStatus.SIGNED.getValue());
        } else {
            entity.setAgreementStatus(dto.getAgreementStatus());
        }

        return entity;
    }

    @Override
    public Page<StaffLabourAgreementDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM StaffLabourAgreement entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.StaffLabourAgreementDto(entity, false) FROM StaffLabourAgreement entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), StaffLabourAgreementDto.class);
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
