package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Department;
import com.buihien.datn.domain.Position;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.dto.DepartmentDto;
import com.buihien.datn.dto.PositionDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.DepartmentRepository;
import com.buihien.datn.repository.PositionRepository;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.DepartmentService;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class DepartmentServiceImpl extends GenericServiceImpl<Department, DepartmentDto, SearchDto> implements DepartmentService {
    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    protected DepartmentDto convertToDto(Department entity) {
        return new DepartmentDto(entity, true, true, true);
    }

    @Override
    protected Department convertToEntity(DepartmentDto dto) {
        return null;
    }

    @Override
    public DepartmentDto saveOrUpdate(DepartmentDto dto) {
        Department entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new Department();
        }

        // Set thông tin cơ bản
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());

        // Set phòng ban cha
        Department parent = null;
        if (dto.getParent() != null && dto.getParent().getId() != null) {
            parent = repository.findById(dto.getParent().getId()).orElse(null);
        }
        if (parent != null && parent.getId() != null && entity.getId() != null) {
            if (entity.getId().equals(parent.getId())) {
                throw new InvalidDataException("Không thể chọn phòng ban hiện tại làm phòng ban cha của chính nó.");
            }
            // Kiểm tra parent có phải là con của entity không (tránh vòng lặp)
            if (isChildDepartment(parent, entity)) {
                throw new InvalidDataException("Không thể chọn một phòng ban con làm phòng ban cha.");
            }
        }
        entity.setParent(parent);

        // Nếu positionManager hiện tại nằm trong danh sách vị trí sẽ bị xóa, thì set null trước
        if (entity.getPositionManager() != null) {
            boolean found = false;
            if (dto.getPositions() != null) {
                for (PositionDto p : dto.getPositions()) {
                    if (p.getId() != null && p.getId().equals(entity.getPositionManager().getId())) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                entity.setPositionManager(null);
            }
        }

        // Xử lý danh sách vị trí
        if (entity.getPositions() == null) {
            entity.setPositions(new HashSet<>());
        } else {
            entity.getPositions().clear(); // Xóa hết để đồng bộ với dto (orphanRemoval sẽ xóa khỏi DB)
        }

        if (dto.getPositions() != null && !dto.getPositions().isEmpty()) {
            for (PositionDto positionDto : dto.getPositions()) {
                Position position = null;
                if (positionDto.getId() != null) {
                    position = positionRepository.findById(positionDto.getId()).orElse(null);
                }
                if (position == null) {
                    position = new Position();
                }
                position.setName(positionDto.getName());
                position.setCode(positionDto.getCode());
                position.setDescription(positionDto.getDescription());
                position.setIsMain(positionDto.getIsMain());
                position.setDepartment(entity);

                Staff staff = null;
                if (positionDto.getStaff() != null && positionDto.getStaff().getId() != null) {
                    staff = staffRepository.findById(positionDto.getStaff().getId()).orElse(null);
                }
                position.setStaff(staff);

                entity.getPositions().add(position);
            }
        }

        // Lưu phòng ban cùng với danh sách vị trí (vì cascade)
        entity = repository.saveAndFlush(entity);

        // Gán lại positionManager (sau khi các vị trí đã được lưu)
        Position positionManager = null;
        if (dto.getPositionManager() != null && dto.getPositionManager().getCode() != null) {
            positionManager = positionRepository.findByCode(dto.getPositionManager().getCode()).orElse(null);
        }
        entity.setPositionManager(positionManager);

        // Lưu lại để cập nhật field positionManager
        entity = repository.save(entity);

        return convertToDto(entity);
    }

    /**
     * Kiểm tra xem parent có phải là phòng ban con (trực tiếp hoặc gián tiếp) của current hay không
     */
    private boolean isChildDepartment(Department parent, Department current) {
        if (current == null || current.getId() == null) return false;

        // Lấy tất cả phòng ban con trực tiếp của current
        List<Department> children = departmentRepository.findByParentId(current.getId());
        for (Department child : children) {
            if (child.getId().equals(parent.getId())) {
                return true; // Phát hiện parent nằm trong cây con
            }
            // Đệ quy kiểm tra sâu hơn
            if (isChildDepartment(parent, child)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Page<DepartmentDto> pagingSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Department entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.DepartmentDto(entity, false, false, false) FROM Department entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
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

        Query q = manager.createQuery(sql.toString(), DepartmentDto.class);
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

    @Override
    public Page<DepartmentDto> pagingTreeSearch(SearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();

        // Query count và dữ liệu danh sách phẳng
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM Department entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.DepartmentDto(entity, false, false, false) FROM Department entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.name) LIKE LOWER(:text) OR LOWER(entity.code) LIKE LOWER(:text)) ");
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
                ? " ORDER BY entity.createdAt ASC"
                : " ORDER BY entity.createdAt DESC");

        Query q = manager.createQuery(sql.toString(), DepartmentDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword().toLowerCase() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword().toLowerCase() + '%');
        }

        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", dto.getFromDate());
            qCount.setParameter("fromDate", dto.getFromDate());
        }
        if (dto.getToDate() != null) {
            q.setParameter("toDate", dto.getToDate());
            qCount.setParameter("toDate", dto.getToDate());
        }

        // Lấy danh sách phẳng toàn bộ dữ liệu (không phân trang từ DB)
        @SuppressWarnings("unchecked")
        List<DepartmentDto> allDepartments = q.getResultList();

        // Xây dựng cây từ danh sách phẳng
        List<DepartmentDto> treeResults = buildTree(allDepartments);

        // Nếu export excel, trả về toàn bộ cây không phân trang
        if (isExportExcel) {
            return new PageImpl<>(treeResults);
        }

        // Phân trang thủ công theo cây (theo các node gốc)
        List<DepartmentDto> pagedResults = manualPaging(treeResults, pageIndex, pageSize);

        // Tổng số node gốc để phân trang chính xác
        long total = treeResults.size();

        return new PageImpl<>(pagedResults, PageRequest.of(pageIndex, pageSize), total);
    }

    private List<DepartmentDto> buildTree(List<DepartmentDto> allDepartments) {
        if (allDepartments == null || allDepartments.isEmpty()) {
            return new ArrayList<>();
        }

        Map<UUID, DepartmentDto> deptMap = new HashMap<>();
        for (DepartmentDto dept : allDepartments) {
            if (dept.getId() != null) {
                deptMap.put(dept.getId(), dept);
            }
        }

        List<DepartmentDto> rootDepartments = new ArrayList<>();

        for (DepartmentDto dept : allDepartments) {
            if (dept.getParentId() == null || !deptMap.containsKey(dept.getParentId())) {
                rootDepartments.add(dept);
            } else {
                DepartmentDto parent = deptMap.get(dept.getParentId());
                if (parent != null) {
                    if (parent.getSubRows() == null) {
                        parent.setSubRows(new ArrayList<>());
                    }
                    parent.getSubRows().add(dept);
                }
            }
        }

        return rootDepartments;
    }

    private List<DepartmentDto> manualPaging(List<DepartmentDto> treeResults, int pageIndex, int pageSize) {
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
