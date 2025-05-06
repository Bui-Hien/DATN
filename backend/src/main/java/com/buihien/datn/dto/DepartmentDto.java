package com.buihien.datn.dto;

import com.buihien.datn.domain.Department;
import com.buihien.datn.domain.Position;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDto extends BaseObjectDto {
    private DepartmentDto parent; // Phòng ban cha
    private Long parentId; // ID phòng ban cha
    private List<DepartmentDto> subDepartments; // Danh sách phòng ban con
    private List<PositionDto> positions; // Các chức danh trong phòng ban
    private PositionDto positionManager; // Vị trí quản lý

    public DepartmentDto() {
    }

    public DepartmentDto(Department entity, Boolean isGetParent, Boolean isGetSub, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
            if (entity.getPositionManager() != null) {
                this.positionManager = new PositionDto(entity.getPositionManager(), false);
            }
            if (isGetParent && entity.getParent() != null) {
                this.parent = new DepartmentDto(entity.getParent(), false, false, isGetFull);
            }

            if (isGetSub && entity.getSubDepartments() != null) {
                this.subDepartments = new ArrayList<>();
                for (Department sub : entity.getSubDepartments()) {
                    this.subDepartments.add(new DepartmentDto(sub, false, true, isGetFull));
                }
            }
            if (isGetFull) {
                if (entity.getPositions() != null && !entity.getPositions().isEmpty()) {
                    this.positions = new ArrayList<>();
                    for (Position dto : entity.getPositions()) {
                        PositionDto positionDto = new PositionDto(dto, false);
                        this.positions.add(positionDto);
                    }
                }
            }
        }
    }

    public List<DepartmentDto> getTreeDepartments(Department entity) {
        List<DepartmentDto> result = new ArrayList<>();
        if (entity == null) {
            return result;
        }

        // Tạo DTO cho entity hiện tại
        DepartmentDto currentDto = new DepartmentDto();
        currentDto.setName(entity.getName());
        currentDto.setCode(entity.getCode());
        currentDto.setDescription(entity.getDescription());
        currentDto.setParentId(entity.getParent() != null ? entity.getParent().getId() : null);
        result.add(currentDto);

        // Xử lý các child nếu có
        if (entity.getSubDepartments() != null && !entity.getSubDepartments().isEmpty()) {
            for (Department child : entity.getSubDepartments()) {
                // Đệ quy để lấy danh sách từ các child
                List<DepartmentDto> childDtos = getTreeDepartments(child);
                result.addAll(childDtos);

                // Set parentId cho các child ngay lập tức
                for (DepartmentDto dto : childDtos) {
                    if (dto.getParentId() == null) {
                        dto.setParentId(entity.getId());
                    }
                }
            }
        }

        return result;
    }

    public DepartmentDto getParent() {
        return parent;
    }

    public void setParent(DepartmentDto parent) {
        this.parent = parent;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<DepartmentDto> getSubDepartments() {
        return subDepartments;
    }

    public void setSubDepartments(List<DepartmentDto> subDepartments) {
        this.subDepartments = subDepartments;
    }

    public List<PositionDto> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionDto> positions) {
        this.positions = positions;
    }

    public PositionDto getPositionManager() {
        return positionManager;
    }

    public void setPositionManager(PositionDto positionManager) {
        this.positionManager = positionManager;
    }
}
