package com.buihien.datn.dto;

import com.buihien.datn.domain.DepartmentIp;

public class DepartmentIpDto extends AuditableDto {
    private DepartmentDto department;// phong ban
    private String ipAddress;// dia chi ip
    private String description;// mo ta

    public DepartmentIpDto() {
    }

    public DepartmentIpDto(DepartmentIp entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.ipAddress = entity.getIpAddress();
            this.description = entity.getDescription();
            if (entity.getDepartment() != null) {
                this.department = new DepartmentDto(entity.getDepartment(), false, false, false);
            }
        }
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
