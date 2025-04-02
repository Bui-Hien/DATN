package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Address;
import com.buihien.datn.domain.Employee;
import com.buihien.datn.dto.RoleDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.dto.test.EmployeeDTO;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.service.EmployeeService;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, EmployeeDTO, SearchDto> implements EmployeeService {
    @Override
    protected EmployeeDTO convertToDto(Employee entity) {
        return new EmployeeDTO(entity);
    }

    @Override
    protected Employee convertToEntity(EmployeeDTO dto) {
        if (dto == null) return null;
        Employee entity = new Employee();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());

        Address a = new Address();
        a.setStreet(dto.getStreet());
        a.setCity(dto.getCity());
        a.setState(dto.getState());
        a.setZipCode(dto.getZipCode());
        a.setCountry(dto.getCountry());
        entity.setAddress(a);
        return entity;
    }

    @Override
    public Page<EmployeeDTO> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Employee entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.test.EmployeeDTO(entity) FROM Employee entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.description) LIKE LOWER(:text)) ");
        }

        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.createDate >= :fromDate ");
        }
        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.createDate <= :toDate ");
        }

        sql.append(whereClause);
        sqlCount.append(whereClause);

        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.createdAt ASC" : " ORDER BY entity.createdAt DESC");

        Query q = manager.createQuery(sql.toString(), EmployeeDTO.class);
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
