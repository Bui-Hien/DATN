package com.buihien.datn.service.impl;

import com.buihien.datn.domain.AdministrativeUnit;
import com.buihien.datn.dto.AdministrativeUnitDto;
import com.buihien.datn.dto.search.AdministrativeUnitSearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.service.AdministrativeUnitService;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AdministrativeUnitServiceImpl extends GenericServiceImpl<AdministrativeUnit, AdministrativeUnitDto, AdministrativeUnitSearchDto> implements AdministrativeUnitService {
    @Override
    protected AdministrativeUnitDto convertToDto(AdministrativeUnit entity) {
        return new AdministrativeUnitDto(entity, false, false);
    }

    @Override
    protected AdministrativeUnit convertToEntity(AdministrativeUnitDto dto) {
        AdministrativeUnit entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new AdministrativeUnit();
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setLevel(dto.getLevel());
        AdministrativeUnit parent = null;
        if (dto.getParent() != null && dto.getParent().getId() != null) {
            parent = repository.findById(dto.getParent().getId()).orElse(null);
        }
        entity.setParent(parent);
        return entity;
    }

    @Override
    public Page<AdministrativeUnitDto> pagingSearch(AdministrativeUnitSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();

        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.AdministrativeUnitDto(entity, false, false) FROM AdministrativeUnit entity ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM AdministrativeUnit entity ");

        // Join để hỗ trợ lọc cấp tỉnh/huyện/xã theo parent
        sql.append(" LEFT JOIN entity.parent parent ");
        sql.append(" LEFT JOIN parent.parent grandParent ");

        sqlCount.append(" LEFT JOIN entity.parent parent ");
        sqlCount.append(" LEFT JOIN parent.parent grandParent ");

        StringBuilder whereClause = new StringBuilder(" WHERE (1=1) ");

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
        }

        if (dto.getLevel() != null) {
            whereClause.append(" AND entity.level = :level ");
        }

        if (dto.getParentId() != null) {
            whereClause.append(" AND entity.parent.id = :parentId ");
        }

        // Phân cấp địa lý
        if (dto.getProvinceId() != null && dto.getDistrictId() == null && dto.getWardId() == null) {
            whereClause.append(" AND (entity.id = :provinceId OR parent.id = :provinceId OR grandParent.id = :provinceId) ");
        }

        if (dto.getProvinceId() != null && dto.getDistrictId() != null && dto.getWardId() == null) {
            whereClause.append(" AND (entity.id = :districtId OR parent.id = :districtId) ");
        }

        if (dto.getProvinceId() != null && dto.getDistrictId() != null && dto.getWardId() != null) {
            whereClause.append(" AND entity.id = :wardId ");
        }

        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.createdAt >= :fromDate ");
        }

        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.createdAt <= :toDate ");
        }

        // Gộp where vào câu truy vấn
        sql.append(whereClause);
        sqlCount.append(whereClause);

        // Sắp xếp
        sql.append(dto.getOrderBy() != null && dto.getOrderBy()
                ? " ORDER BY entity.createdAt ASC"
                : " ORDER BY entity.createdAt DESC");

        // Tạo truy vấn
        Query q = manager.createQuery(sql.toString(), AdministrativeUnitDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        // Set parameters
        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", "%" + dto.getKeyword().toLowerCase() + "%");
            qCount.setParameter("text", "%" + dto.getKeyword().toLowerCase() + "%");
        }

        if (dto.getLevel() != null) {
            q.setParameter("level", dto.getLevel());
            qCount.setParameter("level", dto.getLevel());
        }

        if (dto.getParentId() != null) {
            q.setParameter("parentId", dto.getParentId());
            qCount.setParameter("parentId", dto.getParentId());
        }

        if (dto.getProvinceId() != null && dto.getDistrictId() == null && dto.getWardId() == null) {
            q.setParameter("provinceId", dto.getProvinceId());
            qCount.setParameter("provinceId", dto.getProvinceId());
        }

        if (dto.getProvinceId() != null && dto.getDistrictId() != null && dto.getWardId() == null) {
            q.setParameter("districtId", dto.getDistrictId());
            qCount.setParameter("districtId", dto.getDistrictId());
        }

        if (dto.getProvinceId() != null && dto.getDistrictId() != null && dto.getWardId() != null) {
            q.setParameter("wardId", dto.getWardId());
            qCount.setParameter("wardId", dto.getWardId());
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
        }

        @SuppressWarnings("unchecked")
        List<AdministrativeUnitDto> results = q.getResultList();
        Long total = (Long) qCount.getSingleResult();

        return new PageImpl<>(results, PageRequest.of(pageIndex, pageSize), total);
    }

}
