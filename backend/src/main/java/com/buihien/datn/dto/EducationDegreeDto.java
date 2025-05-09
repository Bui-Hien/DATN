package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.EducationDegree;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

@Valid
public class EducationDegreeDto extends AuditableDto {
    private String code;
    private String name;
    @ValidEnumValue(enumClass = DatnConstants.EducationLevel.class, message = "Trình độ học vấn không hợp lệ")
    private Integer level; //DatnConstants.EducationLevel // Trình độ học vấn (tiến sĩ, thạc sĩ, cử nhân, kỹ sư, trung cấp, cao đẳng, ...)

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
