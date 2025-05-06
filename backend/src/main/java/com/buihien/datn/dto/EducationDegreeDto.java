package com.buihien.datn.dto;

import com.buihien.datn.domain.EducationDegree;

public class EducationDegreeDto extends AuditableDto {
    private String code;
    private String name;
    private Integer level;

    public EducationDegreeDto() {
    }

    public EducationDegreeDto(EducationDegree entity) {
        super(entity);
        if (entity != null) {
            this.code = entity.getCode();
            this.name = entity.getName();
            this.level = entity.getLevel();
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
