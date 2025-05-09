package com.buihien.datn.service.impl;

import com.buihien.datn.domain.SalaryTemplate;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.domain.StaffLabourAgreement;
import com.buihien.datn.dto.StaffLabourAgreementDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.SalaryTemplateRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.StaffLabourAgreementService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StaffLabourAgreementServiceImpl extends GenericServiceImpl<StaffLabourAgreement, StaffLabourAgreementDto, SearchDto> implements StaffLabourAgreementService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private SalaryTemplateRepository salaryTemplateRepository;

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

        SalaryTemplate salaryTemplate = null;
        if (dto.getSalaryTemplate() != null && dto.getSalaryTemplate().getId() != null) {
            salaryTemplate = salaryTemplateRepository.findById(dto.getSalaryTemplate().getId()).orElse(null);
        }
        entity.setSalaryTemplate(salaryTemplate);

        //Thông tin bảo hiển xã hội
        //Kiểm tra xem nhân viên có đóng bảo hiểm xã hội hay không
        if (staff.getHasSocialIns() != null && staff.getHasSocialIns() || dto.getHasSocialIns() != null && dto.getHasSocialIns()) {
            staff.setHasSocialIns(true);
            staff = staffRepository.save(staff);
        }
        if (staff.getHasSocialIns() != null && staff.getHasSocialIns()) {
            entity.setSocialInsuranceNumber(entity.getSocialInsuranceNumber());
            entity.setStartInsDate(entity.getStartInsDate());
            entity.setInsuranceSalary(entity.getInsuranceSalary());

            //Cá nhân đóng bảo hiểm xã hội
            entity.setStaffSocialInsurancePercentage(entity.getStaffSocialInsurancePercentage());
            entity.setStaffHealthInsurancePercentage(entity.getStaffHealthInsurancePercentage());
            entity.setStaffUnemploymentInsurancePercentage(entity.getStaffUnemploymentInsurancePercentage());

            //Công ty đóng bảo hiểm xã hội
            entity.setOrgSocialInsurancePercentage(entity.getOrgSocialInsurancePercentage());
            entity.setOrgHealthInsurancePercentage(entity.getOrgHealthInsurancePercentage());
            entity.setOrgUnemploymentInsurancePercentage(entity.getOrgUnemploymentInsurancePercentage());

            entity.setPaidStatus(entity.getPaidStatus());
            entity.setInsuranceStartDate(entity.getInsuranceStartDate());
            entity.setInsuranceEndDate(entity.getInsuranceEndDate());
            entity.setAgreementStatus(entity.getAgreementStatus());
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
