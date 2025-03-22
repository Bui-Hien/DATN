package com.buihien.datn.dto.base;

import com.buihien.datn.domain.BaseObject;

public class BaseObjectDto extends AuditableEntityDto {
    private String code;
    private String name;
    private String description;

    public BaseObjectDto() {
    }

    public BaseObjectDto(BaseObject entity) {
        super(entity);
        if (entity != null) {
            this.code = entity.getCode();
            this.name = entity.getName();
            this.description = entity.getDescription();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
