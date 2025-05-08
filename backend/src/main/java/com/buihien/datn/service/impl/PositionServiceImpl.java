package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Department;
import com.buihien.datn.domain.Position;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.dto.PositionDto;
import com.buihien.datn.dto.search.PositionSearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.DepartmentRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.PositionService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PositionServiceImpl extends GenericServiceImpl<Position, PositionDto, PositionSearchDto> implements PositionService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    protected PositionDto convertToDto(Position entity) {
        return new PositionDto(entity, true);
    }

    @Override
    protected Position convertToEntity(PositionDto dto) {
        Position entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new Position();
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setMain(dto.getMain());
        Department department = null;
        if (dto.getDepartment() != null && dto.getDepartment().getId() != null) {
            department = departmentRepository.findById(dto.getDepartment().getId()).orElse(null);
        }
        entity.setDepartment(department);

        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            staff = staffRepository.findById(dto.getStaff().getId()).orElse(null);
        }
        entity.setStaff(staff);

        return entity;
    }

    @Override
    public Page<PositionDto> pagingSearch(PositionSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Position entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.PositionDto(entity, false) FROM Position entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
        }
        if (dto.getDepartmentId() != null) {
            whereClause.append(" AND entity.department.id = :departmentId ");
        }
        if (dto.getStaffId() != null) {
            whereClause.append(" AND entity.staff.id = :staffId ");
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

        Query q = manager.createQuery(sql.toString(), PositionDto.class);
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
