package com.buihien.datn.generic;

import com.buihien.datn.domain.AuditableEntity;
import com.buihien.datn.dto.AuditableDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.ResourceNotFoundException;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
public abstract class GenericServiceImpl<E extends AuditableEntity, DTO extends AuditableDto, S extends SearchDto> implements GenericService<DTO, S> {
    private static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);
    @Autowired
    protected JpaRepository<E, UUID> repository;

    @PersistenceContext
    protected EntityManager manager;

    protected abstract DTO convertToDto(E entity);

    protected abstract E convertToEntity(DTO dto);

    @Override
    public DTO getById(UUID id) {
        logger.info("Fetching entity with ID: {}", id);
        Optional<E> entity = repository.findById(id);
        if (entity.isPresent()) {
            logger.info("Successfully fetched entity.");
            return this.convertToDto(entity.get());
        } else {
            logger.warn("Entity not found with ID: {}", id);
            throw new ResourceNotFoundException("Entity not found with ID: " + id); // Ném lỗi nếu không tìm thấy entity
        }
    }

    @Override
    public Boolean deleteById(UUID id) {
        logger.info("Attempting to delete entity with ID: {}", id);
        if (repository.existsById(id)) {
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
        repository.deleteAllById(ids);
        logger.info("Successfully deleted {} entities", ids.size());
        return ids.size();
    }

    @Override
    public DTO saveOrUpdate(DTO dto) {
        logger.info("Attempting to save or update dto: {}", dto);
        if (dto == null) {
            logger.warn("Null dto provided. Cannot save.");
            throw new ResourceNotFoundException("Null dto provided. Cannot save.");
        }
        E savedEntity = repository.save(this.convertToEntity(dto));
        logger.info("Successfully saved entity.");
        return this.convertToDto(savedEntity);

    }

    @Override
    public Integer saveOrUpdateList(List<DTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            logger.warn("Save or update list called with empty data.");
            return 0;
        }

        // Chuyển đổi DTO thành Entity
        List<E> entities = dtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        // Lưu vào database
        List<E> savedEntities = repository.saveAll(entities);

        logger.info("Successfully saved {} records.", savedEntities.size());
        return savedEntities.size();
    }

    @Override
    public Page<DTO> paging(int pageIndex, int pageSize) {
        logger.info("Fetching paginated results: page {} - size {}", pageIndex, pageSize);
        Pageable pageable = PageRequest.of(Math.max(pageIndex - 1, 0), pageSize);
        Page<E> page = repository.findAll(pageable);

        List<DTO> dtoList = page.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        logger.info("Successfully retrieved paginated results: {} items", page.getTotalElements());
        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }
}
