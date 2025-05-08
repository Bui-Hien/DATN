package com.buihien.datn.service.impl;

import com.buihien.datn.domain.AdministrativeUnit;
import com.buihien.datn.domain.Person;
import com.buihien.datn.domain.PersonAddress;
import com.buihien.datn.dto.BankDto;
import com.buihien.datn.dto.PersonAddressDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.AdministrativeUnitRepository;
import com.buihien.datn.repository.PersonAddressRepository;
import com.buihien.datn.repository.PersonRepository;
import com.buihien.datn.service.PersonAddressService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class PersonAddressServiceImpl extends GenericServiceImpl<PersonAddress, PersonAddressDto, SearchDto> implements PersonAddressService {
    @Autowired
    private PersonAddressRepository personAddressRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AdministrativeUnitRepository administrativeUnitRepository;

    @Override
    protected PersonAddressDto convertToDto(PersonAddress entity) {
        return new PersonAddressDto(entity, true);
    }

    @Override
    protected PersonAddress convertToEntity(PersonAddressDto dto) {
        PersonAddress entity = null;

        if (dto.getId() != null) {
            entity = personAddressRepository.findById(dto.getId()).orElse(null);
        }

        if (entity == null) {
            entity = new PersonAddress();
        }

        // Load Person
        Person person = null;
        if (dto.getPerson() != null && dto.getPerson().getId() != null) {
            person = personRepository.findById(dto.getPerson().getId()).orElse(null);
        }
        if (person == null) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }
        entity.setPerson(person);

        // Load AdministrativeUnit theo ưu tiên: ward -> district -> province
        AdministrativeUnit adminUnit = null;
        if (dto.getWard() != null && dto.getWard().getId() != null) {
            adminUnit = administrativeUnitRepository.findById(dto.getWard().getId()).orElse(null);
        } else if (dto.getDistrict() != null && dto.getDistrict().getId() != null) {
            adminUnit = administrativeUnitRepository.findById(dto.getDistrict().getId()).orElse(null);
        } else if (dto.getProvince() != null && dto.getProvince().getId() != null) {
            adminUnit = administrativeUnitRepository.findById(dto.getProvince().getId()).orElse(null);
        }
        entity.setAdminUnit(adminUnit);

        // Các trường còn lại
        entity.setAddressType(dto.getAddressType());
        entity.setAddressDetail(dto.getAddressDetail());

        return entity;
    }


    @Override
    public Page<PersonAddressDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM PersonAddress entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.PersonAddressDto(entity, false) FROM PersonAddress entity WHERE (1=1) ");

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
