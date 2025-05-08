package com.buihien.datn.dto.search;

import java.util.UUID;

public class PositionSearchDto extends SearchDto {
    private UUID departmentId;
    private UUID staffId;

    public PositionSearchDto() {
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }
}
