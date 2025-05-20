package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.AdministrativeUnit;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.*;
import java.util.stream.Collectors;

@Valid
public class AdministrativeUnitDto extends AuditableDto {
    private String name;
    private String code;
    @ValidEnumValue(enumClass = DatnConstants.AdministrativeUnitLevel.class, message = "Cấp hành chính không hợp lệ")
    private Integer level;
    private AdministrativeUnitDto parent;
    private UUID parentId;
    private List<AdministrativeUnitDto> subRows;

    public AdministrativeUnitDto() {
    }

    public AdministrativeUnitDto(AdministrativeUnit entity, Boolean isGetParent, Boolean isGetSub) {
        super(entity);
        if (entity != null) {
            this.name = entity.getName();
            this.code = entity.getCode();
            this.level = entity.getLevel();
            if (entity.getParent() != null) {
                this.parentId = entity.getParent().getId();
            }

            if (isGetParent && entity.getParent() != null) {
                this.parent = new AdministrativeUnitDto(entity.getParent(), false, false);
            }

            if (isGetSub && entity.getSubAdministrativeUnits() != null) {
                this.subRows = new ArrayList<>();
                for (AdministrativeUnit sub : entity.getSubAdministrativeUnits()) {
                    this.subRows.add(new AdministrativeUnitDto(sub, false, true));
                }
            }
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public AdministrativeUnitDto getParent() {
        return parent;
    }

    public void setParent(AdministrativeUnitDto parent) {
        this.parent = parent;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<AdministrativeUnitDto> getSubRows() {
        return subRows;
    }

    public void setSubRows(List<AdministrativeUnitDto> subRows) {
        this.subRows = subRows;
    }
}
