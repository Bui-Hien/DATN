package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.AdministrativeUnit;
import com.buihien.datn.dto.AdministrativeUnitDto;
import com.buihien.datn.dto.search.AdministrativeUnitSearchDto;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.service.AdministrativeUnitService;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdministrativeUnitServiceImpl extends GenericServiceImpl<AdministrativeUnit, AdministrativeUnitDto, AdministrativeUnitSearchDto> implements AdministrativeUnitService {
    @Override
    protected AdministrativeUnitDto convertToDto(AdministrativeUnit entity) {
        return new AdministrativeUnitDto(entity, true, false);
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
    @Transactional
    public Integer saveOrUpdateList(List<AdministrativeUnitDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return 0;
        }

        // Lọc danh sách hợp lệ
        List<AdministrativeUnitDto> validDtos = dtos.stream().filter(this::isValidDto).toList();
        if (validDtos.isEmpty()) return 0;

        // Lấy danh sách entity hiện có
        Map<String, AdministrativeUnit> existingUnits = repository.findAll().stream().collect(Collectors.toMap(AdministrativeUnit::getCode, Function.identity()));

        // Lưu các đơn vị theo thứ tự: Tỉnh (1) → Huyện (2) → Xã (3)
        for (int level = 1; level <= 3; level++) {
            List<AdministrativeUnit> unitsToSave = new ArrayList<>();

            for (AdministrativeUnitDto dto : validDtos) {
                // Bỏ qua nếu không phải level hiện tại
                if (!dto.getLevel().equals(level)) {
                    continue;
                }

                AdministrativeUnit entity = existingUnits.getOrDefault(dto.getCode(), new AdministrativeUnit());
                entity.setCode(dto.getCode());
                entity.setName(dto.getName());
                entity.setLevel(dto.getLevel());

                // Gán parent nếu có
                if (dto.getParentCode() != null && !dto.getParentCode().isEmpty()) {
                    AdministrativeUnit parent = existingUnits.get(dto.getParentCode());
                    if (parent != null) entity.setParent(parent);
                }

                unitsToSave.add(entity);
                existingUnits.put(dto.getCode(), entity); // Cập nhật lại danh sách đã lưu
            }

            // Lưu danh sách đơn vị của level hiện tại
            if (!unitsToSave.isEmpty()) {
                repository.saveAllAndFlush(unitsToSave);
            }
        }

        return validDtos.size();
    }

    private boolean isValidDto(AdministrativeUnitDto dto) {
        return dto != null && dto.getCode() != null && StringUtils.hasText(dto.getCode()) && dto.getName() != null && StringUtils.hasText(dto.getName()) && dto.getLevel() != null;
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
                ? " ORDER BY entity.level DESC, entity.code DESC "
                : " ORDER BY entity.level ASC, entity.code ASC ");

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

        @SuppressWarnings("unchecked") List<AdministrativeUnitDto> results = q.getResultList();
        Long total = (Long) qCount.getSingleResult();

        return new PageImpl<>(results, PageRequest.of(pageIndex, pageSize), total);
    }

    @Override
    public Page<AdministrativeUnitDto> pagingTreeSearch(AdministrativeUnitSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();
        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();

        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.AdministrativeUnitDto(entity, true, false) FROM AdministrativeUnit entity ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM AdministrativeUnit entity ");
        StringBuilder whereClause = new StringBuilder(" WHERE (1=1) ");

        // Điều kiện lọc
        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
        }

        if (dto.getLevel() != null) {
            whereClause.append(" AND entity.level = :level ");
        }

        if (dto.getParentId() != null) {
            whereClause.append(" AND entity.parent.id = :parentId ");
        }

        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.createdAt >= :fromDate ");
        }

        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.createdAt <= :toDate ");
        }

        sql.append(whereClause);
        sqlCount.append(whereClause);
        sql.append(dto.getOrderBy() != null && dto.getOrderBy()
                ? " ORDER BY entity.level DESC, entity.code DESC "
                : " ORDER BY entity.level ASC, entity.code ASC ");
        Query q = manager.createQuery(sql.toString(), AdministrativeUnitDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        // Set tham số
        if (StringUtils.hasText(dto.getKeyword())) {
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

        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", dto.getFromDate());
            qCount.setParameter("fromDate", dto.getFromDate());
        }

        if (dto.getToDate() != null) {
            q.setParameter("toDate", dto.getToDate());
            qCount.setParameter("toDate", dto.getToDate());
        }

        // Lấy danh sách phẳng
        @SuppressWarnings("unchecked")
        List<AdministrativeUnitDto> flatList = q.getResultList();

        // Dựng cây
        List<AdministrativeUnitDto> treeResults = buildTree(flatList);

        // Trả toàn bộ cây nếu export excel
        if (isExportExcel) {
            return new PageImpl<>(treeResults);
        }

        // Phân trang thủ công trên các node gốc
        List<AdministrativeUnitDto> pagedResults = manualPaging(treeResults, pageIndex, pageSize);

        return new PageImpl<>(pagedResults, PageRequest.of(pageIndex, pageSize), treeResults.size());
    }

    private List<AdministrativeUnitDto> buildTree(List<AdministrativeUnitDto> units) {
        Map<UUID, AdministrativeUnitDto> map = new HashMap<>();
        List<AdministrativeUnitDto> roots = new ArrayList<>();

        for (AdministrativeUnitDto dto : units) {
            map.put(dto.getId(), dto);
        }

        for (AdministrativeUnitDto dto : units) {
            if (dto.getParentId() != null && map.containsKey(dto.getParentId())) {
                AdministrativeUnitDto parent = map.get(dto.getParentId());
                if (parent.getSubRows() == null) {
                    parent.setSubRows(new ArrayList<>());
                }
                parent.getSubRows().add(dto);
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }

    private List<AdministrativeUnitDto> manualPaging(List<AdministrativeUnitDto> treeResults, int pageIndex, int pageSize) {
        if (treeResults == null || treeResults.isEmpty()) {
            return new ArrayList<>();
        }

        int startIndex = pageIndex * pageSize;
        int endIndex = Math.min(startIndex + pageSize, treeResults.size());

        if (startIndex >= treeResults.size()) {
            return new ArrayList<>();
        }

        return treeResults.subList(startIndex, endIndex);
    }


}
