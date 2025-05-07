package com.buihien.datn.dto.search;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.validator.ValidEnumValue;

import java.util.UUID;

public class AdministrativeUnitSearchDto extends SearchDto {
    @ValidEnumValue(enumClass = DatnConstants.AdministrativeUnitLevel.class, message = "Cấp hành chính không hợp lệ")
    private Integer level; // DatnConstants.AdministrativeUnitLevel
    private UUID provinceId;
    private UUID districtId;
    private UUID wardId;

    public AdministrativeUnitSearchDto() {
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public UUID getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(UUID provinceId) {
        this.provinceId = provinceId;
    }

    public UUID getDistrictId() {
        return districtId;
    }

    public void setDistrictId(UUID districtId) {
        this.districtId = districtId;
    }

    public UUID getWardId() {
        return wardId;
    }

    public void setWardId(UUID wardId) {
        this.wardId = wardId;
    }
}
