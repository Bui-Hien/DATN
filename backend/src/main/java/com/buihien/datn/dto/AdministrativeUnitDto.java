package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.AdministrativeUnit;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@Valid
public class AdministrativeUnitDto extends AuditableDto {
    private String name;
    private String code;
    @ValidEnumValue(enumClass = DatnConstants.AdministrativeUnitLevel.class, message = "Cấp hành chính không hợp lệ")
    private Integer level;
    private AdministrativeUnitDto parent;
    private Long parentId;
    private List<AdministrativeUnitDto> subAdministrativeUnits;

    public AdministrativeUnitDto() {
    }

    public AdministrativeUnitDto(AdministrativeUnit entity, Boolean isGetParent, Boolean isGetSub) {
        super(entity);
        if (entity != null) {
            this.name = entity.getName();
            this.code = entity.getCode();
            this.level = entity.getLevel();

            if (isGetParent && entity.getParent() != null) {
                this.parent = new AdministrativeUnitDto(entity.getParent(), false, false);
            }

            if (isGetSub && entity.getSubAdministrativeUnits() != null) {
                this.subAdministrativeUnits = new ArrayList<>();
                for (AdministrativeUnit sub : entity.getSubAdministrativeUnits()) {
                    this.subAdministrativeUnits.add(new AdministrativeUnitDto(sub, false, true));
                }
            }
        }
    }

    public List<AdministrativeUnitDto> getTreeAdministrativeUnits(AdministrativeUnit entity) {
        List<AdministrativeUnitDto> result = new ArrayList<>();
        if (entity == null) {
            return result;
        }

        // Tạo DTO cho entity hiện tại
        AdministrativeUnitDto currentDto = new AdministrativeUnitDto();
        currentDto.setName(entity.getName());
        currentDto.setCode(entity.getCode());
        currentDto.setLevel(entity.getLevel());
        currentDto.setParentId(entity.getParent() != null ? entity.getParent().getId() : null);
        result.add(currentDto);

        // Xử lý các child nếu có
        if (entity.getSubAdministrativeUnits() != null && !entity.getSubAdministrativeUnits().isEmpty()) {
            for (AdministrativeUnit child : entity.getSubAdministrativeUnits()) {
                // Đệ quy để lấy danh sách từ các child
                List<AdministrativeUnitDto> childDtos = getTreeAdministrativeUnits(child);
                result.addAll(childDtos);

                // Set parentId cho các child ngay lập tức
                for (AdministrativeUnitDto dto : childDtos) {
                    if (dto.getParentId() == null) {
                        dto.setParentId(entity.getId());
                    }
                }
            }
        }

        return result;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<AdministrativeUnitDto> getSubAdministrativeUnits() {
        return subAdministrativeUnits;
    }

    public void setSubAdministrativeUnits(List<AdministrativeUnitDto> subAdministrativeUnits) {
        this.subAdministrativeUnits = subAdministrativeUnits;
    }
}
