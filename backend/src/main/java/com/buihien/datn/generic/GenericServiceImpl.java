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
        logger.info("Đang tìm kiếm thực thể với ID: {}", id);
        Optional<E> entity = repository.findById(id);
        if (entity.isPresent()) {
            logger.info("Lấy thực thể thành công.");
            return this.convertToDto(entity.get());
        } else {
            logger.warn("Không tìm thấy thực thể với ID: {}", id);
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu với ID: " + id);
        }
    }

    @Override
    public Boolean deleteById(UUID id) {
        logger.info("Đang xóa thực thể với ID: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            logger.info("Đã xóa thực thể thành công với ID: {}", id);
            return true;
        } else {
            logger.warn("Không tìm thấy thực thể để xóa với ID: {}", id);
            throw new ResourceNotFoundException("Không tìm thấy dữ liệu để xóa với ID: " + id);
        }
    }

    @Override
    public int deleteMultiple(List<UUID> ids) {
        logger.info("Đang xóa nhiều thực thể với các ID: {}", ids);
        if (ids == null || ids.isEmpty()) {
            logger.warn("Danh sách ID rỗng. Không có thực thể nào để xóa.");
            throw new ResourceNotFoundException("Danh sách ID rỗng. Không có thực thể nào để xóa.");
        }
        repository.deleteAllById(ids);
        logger.info("Đã xóa thành công {} thực thể.", ids.size());
        return ids.size();
    }

    @Override
    public DTO saveOrUpdate(DTO dto) {
        if (dto == null) {
            logger.warn("Thông tin đầu vào bị null. Không thể lưu.");
            throw new ResourceNotFoundException("Thông tin đầu vào bị null. Không thể lưu.");
        }

        E savedEntity = repository.save(this.convertToEntity(dto));

        if (dto.getId() != null) {
            logger.info("Cập nhật thông tin thành công: {}", savedEntity.getId());
        } else {
            logger.info("Lưu thông tin mới thành công: {}", savedEntity.getId());
        }

        return this.convertToDto(savedEntity);
    }


    @Override
    public Integer saveOrUpdateList(List<DTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            logger.warn("Danh sách dữ liệu trống. Không thể lưu.");
            return 0;
        }

        // Chuyển đổi DTO thành Entity
        List<E> entities = dtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());

        // Lưu vào database
        List<E> savedEntities = repository.saveAll(entities);

        logger.info("Đã lưu thành công {} bản ghi.", savedEntities.size());
        return savedEntities.size();
    }
    @Override
    public Page<DTO> paging(int pageIndex, int pageSize) {
        logger.info("Lấy danh sách phân trang: trang {} - kích thước {}", pageIndex, pageSize);

        Pageable pageable = PageRequest.of(Math.max(pageIndex - 1, 0), pageSize);
        Page<E> page = repository.findAll(pageable);

        List<DTO> dtoList = page.getContent()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        logger.info("Lấy dữ liệu thành công: tổng cộng {} mục", page.getTotalElements());

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }
}
