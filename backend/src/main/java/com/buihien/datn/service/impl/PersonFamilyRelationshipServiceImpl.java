package com.buihien.datn.service.impl;

import com.buihien.datn.domain.FamilyRelationship;
import com.buihien.datn.domain.Person;
import com.buihien.datn.domain.PersonFamilyRelationship;
import com.buihien.datn.domain.Profession;
import com.buihien.datn.dto.PersonFamilyRelationshipDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.FamilyRelationshipRepository;
import com.buihien.datn.repository.PersonRepository;
import com.buihien.datn.repository.ProfessionRepository;
import com.buihien.datn.service.PersonFamilyRelationshipService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PersonFamilyRelationshipServiceImpl extends GenericServiceImpl<PersonFamilyRelationship, PersonFamilyRelationshipDto, SearchDto> implements PersonFamilyRelationshipService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FamilyRelationshipRepository familyRelationshipRepository;
    @Autowired
    private ProfessionRepository professionRepository;

    @Override
    protected PersonFamilyRelationshipDto convertToDto(PersonFamilyRelationship entity) {
        return new PersonFamilyRelationshipDto(entity, true);
    }

    @Override
    protected PersonFamilyRelationship convertToEntity(PersonFamilyRelationshipDto dto) {
        PersonFamilyRelationship entity = null;

        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }

        if (entity == null) {
            entity = new PersonFamilyRelationship();
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

        // Load FamilyRelationship
        FamilyRelationship familyRelationship = null;
        if (dto.getFamilyRelationship() != null && dto.getFamilyRelationship().getId() != null) {
            familyRelationship = familyRelationshipRepository.findById(dto.getFamilyRelationship().getId()).orElse(null);
        }
        if (familyRelationship == null) {
            throw new ResourceNotFoundException("Mối quan hệ gia đình không tồn tại");
        }
        entity.setFamilyRelationship(familyRelationship);
        entity.setFullName(dto.getFullName());
        entity.setAddress(dto.getAddress());
        entity.setBirthDate(dto.getBirthDate());

        // Load Profession
        Profession profession = null;
        if (dto.getProfession() != null && dto.getProfession().getId() != null) {
            profession = professionRepository.findById(dto.getProfession().getId()).orElse(null);
        }
        entity.setProfession(profession);

        return entity;
    }


    @Override
    public Page<PersonFamilyRelationshipDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM PersonFamilyRelationship entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.PersonFamilyRelationshipDto(entity, false) FROM PersonFamilyRelationship entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.fullName) LIKE LOWER(:text)) ");
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

        Query q = manager.createQuery(sql.toString(), PersonFamilyRelationshipDto.class);
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
