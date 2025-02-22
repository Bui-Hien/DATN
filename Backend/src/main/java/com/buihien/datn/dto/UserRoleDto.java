package com.buihien.datn.dto;

import com.buihien.datn.domain.Role;
import com.buihien.datn.domain.UserRole;

public class UserRoleDto extends AuditableEntityDto {
    private RoleDto role;

    public UserRoleDto() {
    }

    public UserRoleDto(UserRole entity) {
        super(entity);
        if (entity != null) {
            this.role = new RoleDto(entity.getRole());
        }
    }

    public UserRoleDto(Role entity) {
        super(entity);
        if (entity != null) {
            this.role = new RoleDto(entity);
        }
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }
}
