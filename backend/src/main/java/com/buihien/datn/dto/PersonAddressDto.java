package com.buihien.datn.dto;

import com.buihien.datn.domain.PersonAddress;
import jakarta.validation.Valid;

@Valid
public class PersonAddressDto extends AuditableDto {
    private String addressDetail;
    private AdministrativeUnitDto province;
    private AdministrativeUnitDto district;
    private AdministrativeUnitDto ward;

    public PersonAddressDto() {
    }

    public PersonAddressDto(PersonAddress entity) {
        super(entity);
        if (entity != null) {
            this.addressDetail = entity.getAddressDetail();
            if (entity.getProvince() != null) {
                this.province = new AdministrativeUnitDto(entity.getProvince(), false, false);
            }
            if (entity.getDistrict() != null) {
                this.district = new AdministrativeUnitDto(entity.getDistrict(), false, false);
            }
            if (entity.getWard() != null) {
                this.ward = new AdministrativeUnitDto(entity.getWard(), false, false);
            }
        }
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public AdministrativeUnitDto getProvince() {
        return province;
    }

    public void setProvince(AdministrativeUnitDto province) {
        this.province = province;
    }

    public AdministrativeUnitDto getDistrict() {
        return district;
    }

    public void setDistrict(AdministrativeUnitDto district) {
        this.district = district;
    }

    public AdministrativeUnitDto getWard() {
        return ward;
    }

    public void setWard(AdministrativeUnitDto ward) {
        this.ward = ward;
    }

}
