package com.buihien.datn.dto;

import com.buihien.datn.domain.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DepartmentDto extends BaseObjectDto {
    private DepartmentDto parent; // Phòng ban cha
    private UUID parentId; // ID phòng ban cha
    private List<DepartmentDto> subRows; // Danh sách phòng ban con
    private StaffDto staffManager; // Vị trí quản lý

    public DepartmentDto() {
    }

    public DepartmentDto(Department entity, Boolean isGetParent, Boolean isGetSub, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.parentId = entity.getParent() != null ? entity.getParent().getId() : null;
            if (entity.getStaffManager() != null) {
                this.staffManager = new StaffDto(entity.getStaffManager(), false);
            }
            if (isGetParent && entity.getParent() != null) {
                this.parent = new DepartmentDto(entity.getParent(), false, false, isGetFull);
            }

            if (isGetSub && entity.getSubDepartments() != null) {
                this.subRows = new ArrayList<>();
                for (Department sub : entity.getSubDepartments()) {
                    this.subRows.add(new DepartmentDto(sub, false, true, isGetFull));
                }
            }
        }
    }

    public DepartmentDto getParent() {
        return parent;
    }

    public void setParent(DepartmentDto parent) {
        this.parent = parent;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<DepartmentDto> getSubRows() {
        return subRows;
    }

    public void setSubRows(List<DepartmentDto> subRows) {
        this.subRows = subRows;
    }

    public StaffDto getStaffManager() {
        return staffManager;
    }

    public void setStaffManager(StaffDto staffManager) {
        this.staffManager = staffManager;
    }
}
