package com.buihien.datn.dto;

import com.buihien.datn.domain.Role;
import com.buihien.datn.util.anotation.Excel;
import com.buihien.datn.util.anotation.ExcelColumnGetter;
import com.buihien.datn.util.anotation.ExcelColumnSetter;

@Excel(name = "")
public class RoleDto extends AuditableDto {
    private String name;
    private String description;

    public RoleDto() {
    }

    public RoleDto(Role entity) {
        super(entity);
        if (entity != null) {
            this.name = entity.getName();
            this.description = entity.getDescription();
        }
    }

    @ExcelColumnGetter(index = 1, title = "Name", width = 20)
    public String getName() {
        return name;
    }

    @ExcelColumnSetter(index = 0)
    public void setName(String name) {
        this.name = name;
    }

    @ExcelColumnGetter(index = 2, title = "Description", width = 25)
    public String getDescription() {
        return description;
    }

    @ExcelColumnSetter(index = 1)
    public void setDescription(String description) {
        this.description = description;
    }
}
