package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Bank;
import com.buihien.datn.domain.Person;
import com.buihien.datn.domain.PersonBankAccount;
import com.buihien.datn.dto.BankDto;
import com.buihien.datn.dto.PersonBankAccountDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.BankRepository;
import com.buihien.datn.repository.PersonBankAccountRepository;
import com.buihien.datn.repository.PersonRepository;
import com.buihien.datn.service.PersonBankAccountService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonBankAccountServiceImpl extends GenericServiceImpl<PersonBankAccount, PersonBankAccountDto, SearchDto> implements PersonBankAccountService {
    @Autowired
    private PersonBankAccountRepository personBankAccountRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BankRepository bankRepository;

    @Override
    protected PersonBankAccountDto convertToDto(PersonBankAccount entity) {
        return new PersonBankAccountDto(entity, true);
    }

    @Override
    protected PersonBankAccount convertToEntity(PersonBankAccountDto dto) {
        PersonBankAccount entity = null;
        if (dto.getId() != null) {
            entity = personBankAccountRepository.findById(dto.getId()).orElse(null);
        }
        if (entity == null && dto.getPerson() != null && dto.getPerson().getId() != null && dto.getBank() != null && dto.getBank().getId() != null) {
            entity = personBankAccountRepository.findByPersonIdAndBankId(dto.getPerson().getId(), dto.getBank().getId());
        }
        if (entity == null) {
            entity = new PersonBankAccount();
        }
        Person person = null;
        if (dto.getPerson() != null && dto.getPerson().getId() != null) {
            person = personRepository.findById(dto.getPerson().getId()).orElse(null);
        }
        if (person == null) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }
        entity.setPerson(person);

        Bank bank = null;
        if (dto.getBank() != null && dto.getBank().getId() != null) {
            bank = bankRepository.findById(dto.getBank().getId()).orElse(null);
        }
        if (bank == null) {
            throw new ResourceNotFoundException("Ngân hàng không tồn tại");
        }
        entity.setBank(bank);
        entity.setBankAccountName(dto.getBankAccountName());
        entity.setBankAccountNumber(dto.getBankAccountNumber());
        entity.setBankBranch(dto.getBankBranch());

        if (dto.getIsMain() != null && dto.getIsMain()) {
            List<PersonBankAccount> personBankAccountList = personBankAccountRepository.findPersonBankAccountByIsMain(entity.getPerson().getId());
            if (personBankAccountList != null && !personBankAccountList.isEmpty()) {
                List<PersonBankAccount> personBankAccounts = new ArrayList<>();
                for (PersonBankAccount item : personBankAccountList) {
                    item.setIsMain(false);
                    personBankAccounts.add(item);
                }
                personBankAccountRepository.saveAll(personBankAccounts);
            }
            entity.setIsMain(dto.getIsMain());
        } else {
            entity.setIsMain(false);
        }
        return entity;
    }

    @Override
    public Page<PersonBankAccountDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM PersonBankAccount entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.PersonBankAccountDto(entity, false) FROM PersonBankAccount entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), PersonBankAccountDto.class);
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
