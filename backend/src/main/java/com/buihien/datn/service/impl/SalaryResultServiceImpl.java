package com.buihien.datn.service.impl;

import com.buihien.datn.domain.*;
import com.buihien.datn.dto.SalaryResultDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.SalaryPeriodRepository;
import com.buihien.datn.repository.SalaryResultItemRepository;
import com.buihien.datn.repository.SalaryTemplateRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.SalaryResultItemDetailService;
import com.buihien.datn.service.SalaryResultService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class SalaryResultServiceImpl extends GenericServiceImpl<SalaryResult, SalaryResultDto, SearchDto> implements SalaryResultService {
    @Autowired
    private SalaryPeriodRepository salaryPeriodRepository;
    @Autowired
    private SalaryResultItemRepository salaryResultItemRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private SalaryTemplateRepository salaryTemplateRepository;
    @Autowired
    private SalaryResultItemDetailService salaryResultItemDetailService;

    @Override
    protected SalaryResultDto convertToDto(SalaryResult entity) {
        return new SalaryResultDto(entity, true);
    }

    @Override
    protected SalaryResult convertToEntity(SalaryResultDto dto) {
        SalaryResult entity = null;
        boolean isCreated = false;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
            isCreated = true;
        }
        if (entity == null) {
            entity = new SalaryResult();
        }
        entity.setName(dto.getName());
        SalaryPeriod salaryPeriod = null;
        if (dto.getSalaryPeriod() != null && dto.getSalaryPeriod().getId() != null) {
            salaryPeriod = salaryPeriodRepository.findById(dto.getSalaryPeriod().getId()).orElse(null);
        }
        if (salaryPeriod == null) {
            throw new ResourceNotFoundException("Kỳ lương không tồn tại");
        }
        entity.setSalaryPeriod(salaryPeriod);


        SalaryTemplate salaryTemplate = null;
        if (dto.getSalaryTemplate() != null && dto.getSalaryTemplate().getId() != null) {
            salaryTemplate = salaryTemplateRepository.findById(dto.getSalaryTemplate().getId()).orElse(null);
        }
        if (salaryTemplate == null) {
            throw new ResourceNotFoundException("Mẫu bảng lương không tồn tại");
        }
        entity.setSalaryTemplate(salaryTemplate);

        if (isCreated) {
            if (entity.getSalaryResultItems() == null) {
                entity.setSalaryResultItems(new HashSet<>());
            }
            entity.getSalaryResultItems().clear();

            //Lấy tất cả nhân viên sử dụng mẫu bảng lương
            List<Staff> staffList = staffRepository.findAllStaffBySalaryTemplateId(salaryTemplate.getId());
            if (staffList == null || staffList.isEmpty()) {
                throw new ResourceNotFoundException("Bảng lương hiện tại không có nhân viên nào sử dụng");
            }
            for (Staff staff : staffList) {
                SalaryResultItem salaryResultItem = salaryResultItemRepository.findSalaryResultItemByStaffIdAndSalaryResultId(staff.getId(), entity.getId());
                if (salaryResultItem == null) {
                    salaryResultItem = new SalaryResultItem();
                }
                salaryResultItem.setStaff(staff);
                salaryResultItem.setSalaryResult(entity);
                salaryResultItemDetailService.handleSetUpSalaryResultItemDetailByStaff(salaryResultItem, salaryTemplate);
                entity.getSalaryResultItems().add(salaryResultItem);
            }
        }
        return entity;
    }

    @Override
    public Page<SalaryResultDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM SalaryResult entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.SalaryResultDto(entity, false) FROM SalaryResult entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), SalaryResultDto.class);
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

    @Override
    public Boolean getRecalculateSalary(UUID salaryResultId) {
        SalaryResult entity = null;
        if (salaryResultId != null) {
            entity = repository.findById(salaryResultId).orElse(null);
        }
        if (entity == null) {
            throw new ResourceNotFoundException("Không tìm thấy bảng lương cần tính lại");
        }
        if (entity.getSalaryPeriod() == null) {
            throw new ResourceNotFoundException("Kỳ lương không tồn tại");
        }

        if (entity.getSalaryTemplate() == null) {
            throw new ResourceNotFoundException("Mẫu bảng lương không tồn tại");
        }

        if (entity.getSalaryResultItems() == null) {
            entity.setSalaryResultItems(new HashSet<>());
        }
        entity.getSalaryResultItems().clear();

        //Lấy tất cả nhân viên sử dụng mẫu bảng lương
        List<Staff> staffList = staffRepository.findAllStaffBySalaryTemplateId(entity.getSalaryTemplate().getId());
        if (staffList == null || staffList.isEmpty()) {
            throw new ResourceNotFoundException("Bảng lương hiện tại không có nhân viên nào sử dụng");
        }
        for (Staff staff : staffList) {
            SalaryResultItem salaryResultItem = salaryResultItemRepository.findSalaryResultItemByStaffIdAndSalaryResultId(staff.getId(), entity.getId());
            if (salaryResultItem == null) {
                salaryResultItem = new SalaryResultItem();
            }
            salaryResultItem.setStaff(staff);
            salaryResultItem.setSalaryResult(entity);
            salaryResultItemDetailService.handleSetUpSalaryResultItemDetailByStaff(salaryResultItem, entity.getSalaryTemplate());
            entity.getSalaryResultItems().add(salaryResultItem);
        }
        return true;
    }
}
