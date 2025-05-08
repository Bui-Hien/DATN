package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Certificate;
import com.buihien.datn.domain.FileDescription;
import com.buihien.datn.domain.Person;
import com.buihien.datn.dto.CertificateDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.PersonRepository;
import com.buihien.datn.service.CertificateService;
import com.buihien.datn.service.FileDescriptionService;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class CertificateServiceImpl extends GenericServiceImpl<Certificate, CertificateDto, SearchDto> implements CertificateService {
    private static final Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);

    @Autowired
    private FileDescriptionService fileDescriptionService;
    @Autowired
    private PersonRepository personRepository;

    @Override
    protected CertificateDto convertToDto(Certificate entity) {
        return new CertificateDto(entity, true);
    }

    @Override
    protected Certificate convertToEntity(CertificateDto dto) {
        Certificate entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new Certificate();
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());

        Person person = null;
        if (dto.getPerson() != null && dto.getPerson().getId() != null) {
            person = personRepository.findById(dto.getPerson().getId()).orElse(null);
        }
        if (person == null) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }

        FileDescription newFile = null;
        if (dto.getCertificateFile() != null && dto.getCertificateFile().getId() != null) {
            newFile = fileDescriptionService.getEntityById(dto.getCertificateFile().getId());
        }

        FileDescription oldFile = entity.getCertificateFile();

        if (oldFile != null && oldFile.getId() != null) {
            // Nếu file mới khác file cũ thì xóa file cũ
            if (newFile == null || !oldFile.getId().equals(newFile.getId())) {
                fileDescriptionService.deleteById(oldFile.getId());
            }
        }

        // Gán file mới (có thể null)
        entity.setCertificateFile(newFile);

        return entity;
    }

    @Override
    public Boolean deleteById(UUID id) {
        logger.info("Attempting to delete entity with ID: {}", id);
        Certificate entity = repository.findById(id).orElse(null);
        if (entity != null) {
            if (entity.getCertificateFile() != null && entity.getCertificateFile().getId() != null) {
                // Xóa file đính kèm nếu có
                fileDescriptionService.deleteById(entity.getCertificateFile().getId());
            }
            // Xóa entity
            repository.deleteById(id);
            logger.info("Successfully deleted entity with ID: {}", id);
            return true;
        } else {
            logger.warn("Entity not found for deletion with ID: {}", id);
            throw new ResourceNotFoundException("Entity not found for deletion with ID: " + id);
        }
    }

    @Override
    public int deleteMultiple(List<UUID> ids) {
        logger.info("Attempting to delete multiple entities with IDs: {}", ids);
        if (ids == null || ids.isEmpty()) {
            logger.warn("Empty ID list provided. No entities to delete.");
            throw new ResourceNotFoundException("Empty ID list provided. No entities to delete.");
        }
        for (UUID id : ids) {
            Certificate entity = repository.findById(id).orElse(null);
            if (entity != null && entity.getCertificateFile() != null && entity.getCertificateFile().getId() != null) {
                // Xóa file đính kèm nếu có
                fileDescriptionService.deleteById(entity.getCertificateFile().getId());
            }
        }
        repository.deleteAllById(ids);
        logger.info("Successfully deleted {} entities", ids.size());
        return ids.size();
    }

    @Override
    public Page<CertificateDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Certificate entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.CertificateDto(entity, false) FROM Certificate entity WHERE (1=1) ");

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

        Query q = manager.createQuery(sql.toString(), CertificateDto.class);
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
