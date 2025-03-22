package com.buihien.datn.service.generic;

import com.buihien.datn.domain.AuditableEntity;
import com.buihien.datn.dto.AuditableEntityDto;
import com.buihien.datn.dto.search.SearchDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public abstract class GenericServiceImpl<E extends AuditableEntity, DTO extends AuditableEntityDto, S extends SearchDto> implements GenericService<DTO, S> {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    @Autowired
    protected JpaRepository<E, Long> repository;

    @PersistenceContext
    protected EntityManager manager;

    protected abstract DTO convertToDto(E entity);

    protected abstract E convertToEntity(DTO dto);

    @Override
    public Boolean deleteById(Long id) {
        logger.info("Attempting to delete entity with ID: {}", id);
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                logger.info("Successfully deleted entity with ID: {}", id);
                return true;
            } else {
                logger.warn("Entity not found for deletion with ID: {}", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error deleting entity with ID: {}", id, e);
            return false;
        }
    }

    @Override
    public int deleteMultiple(List<Long> ids) {
        logger.info("Attempting to delete multiple entities with IDs: {}", ids);
        if (ids == null || ids.isEmpty()) {
            logger.warn("Empty ID list provided. No entities to delete.");
            return 0;
        }
        try {
            repository.deleteAllById(ids);
            logger.info("Successfully deleted {} entities", ids.size());
            return ids.size();
        } catch (Exception e) {
            logger.error("Error deleting multiple entities with IDs: {}", ids, e);
            return 0;
        }
    }

    @Override
    public DTO saveOrUpdate(DTO dto) {
        logger.info("Attempting to save or update dto: {}", dto);
        if (dto == null) {
            logger.warn("Null dto provided. Cannot save.");
            return null;
        }
        try {
            E savedEntity = repository.save(this.convertToEntity(dto));
            logger.info("Successfully saved entity.");
            return this.convertToDto(savedEntity);
        } catch (Exception e) {
            logger.error("Error saving or updating dto: {}", dto, e);
            return null;
        }
    }

    @Override
    public DTO getById(Long id) {
        logger.info("Fetching entity with ID: {}", id);
        try {
            Optional<E> entity = repository.findById(id);
            if (entity.isPresent()) {
                logger.info("Successfully fetched entity.");
                return this.convertToDto(entity.get());
            } else {
                logger.warn("Entity not found with ID: {}", id);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching entity with ID: {}", id, e);
            return null;
        }
    }

    @Override
    public Page<DTO> paging(int pageIndex, int pageSize) {
        logger.info("Fetching paginated results: page {} - size {}", pageIndex, pageSize);
        try {
            Pageable pageable = PageRequest.of(Math.max(pageIndex - 1, 0), pageSize);
            Page<E> page = repository.findAll(pageable);

            List<DTO> dtoList = page.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

            logger.info("Successfully retrieved paginated results: {} items", page.getTotalElements());
            return new PageImpl<>(dtoList, pageable, page.getTotalElements());
        } catch (Exception e) {
            logger.error("Error fetching paginated results: page {} - size {}", pageIndex, pageSize, e);
            return Page.empty();
        }
    }

    @Override
    public Page<DTO> pagingSearch(S dto) {
        return Page.empty();
    }
}
