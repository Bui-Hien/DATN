package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.AdministrativeUnit;
import com.buihien.datn.dto.validator.ValidEnumValue;
import com.buihien.datn.util.anotation.Excel;
import com.buihien.datn.util.anotation.ExcelColumnGetter;
import com.buihien.datn.util.anotation.ExcelColumnSetter;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Valid
@Excel(name = "DU_LIEU_DON_VI_HANH_CHINH", numericalOrder = true, numericalOrderName = "STT")
public class AdministrativeUnitDto extends AuditableDto {
    private String code;
    private String name;
    @ValidEnumValue(enumClass = DatnConstants.AdministrativeUnitLevel.class, message = "Cấp hành chính không hợp lệ")
    private Integer level;
    private String parentCode;
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


    @ExcelColumnGetter(index = 0, title = "Mã đơn vị hành chính(*)")
    public String getCode() {
        return code;
    }

    @ExcelColumnSetter(index = 0)
    public void setCode(String code) {
        this.code = code;
    }

    @ExcelColumnGetter(index = 1, title = "Tên đơn vị hành chính")
    public String getName() {
        return name;
    }
    @ExcelColumnSetter(index = 1)
    public void setName(String name) {
        this.name = name;
    }
    @ExcelColumnGetter(index = 2, title = "Mã cấp độ(*)")
    public Integer getLevel() {
        return level;
    }
    @ExcelColumnSetter(index = 2)
    public void setLevel(Integer level) {
        this.level = level;
    }

    @ExcelColumnGetter(index = 4, title = "Mã đơn vị quản lý")
    public String getParentCode() {
        return parentCode;
    }

    @ExcelColumnSetter(index = 4)
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
