package com.buihien.datn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "tbl_education_degree")
@Entity
public class EducationDegree extends AuditableEntity {
    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "level")
    private Integer level; //DatnConstants.EducationLevel // Trình độ học vấn (tiến sĩ, thạc sĩ, cử nhân, kỹ sư, trung cấp, cao đẳng, ...)

    public EducationDegree() {
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
