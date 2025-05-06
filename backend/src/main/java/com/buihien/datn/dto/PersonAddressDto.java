package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.PersonAddress;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

@Valid
public class PersonAddressDto extends AuditableDto {
    @ValidEnumValue(enumClass = DatnConstants.AddressType.class, message = "Loại địa chỉ không hợp lệ")
    private Integer addressType;
    private String addressDetail;
    private AdministrativeUnitDto province;
    private AdministrativeUnitDto district;
    private AdministrativeUnitDto ward;
    private PersonDto person;

    public PersonAddressDto() {
    }

    public PersonAddressDto(PersonAddress entity, Boolean isGetPerson) {
        super(entity);
        if (entity != null) {
            this.addressType = entity.getAddressType();
            this.addressDetail = entity.getAddressDetail();
            if (isGetPerson) {
                this.person = entity.getPerson() != null ? new PersonDto(entity.getPerson(), false) : null;
            }
            if (entity.getAdminUnit() != null && entity.getAdminUnit().getLevel() != null) {
                if (DatnConstants.AdministrativeUnitLevel.WARD.getValue().equals(entity.getAdminUnit().getLevel())) {
                    this.ward = new AdministrativeUnitDto(entity.getAdminUnit(), false, false);

                    if (entity.getAdminUnit().getParent() != null && entity.getAdminUnit().getParent().getLevel() != null) {
                        if (DatnConstants.AdministrativeUnitLevel.DISTRICT.getValue().equals(entity.getAdminUnit().getParent().getLevel())) {
                            this.district = new AdministrativeUnitDto(entity.getAdminUnit().getParent(),false, false);

                            if (entity.getAdminUnit().getParent().getParent() != null &&
                                    entity.getAdminUnit().getParent().getParent().getLevel() != null) {
                                if (DatnConstants.AdministrativeUnitLevel.PROVINCE.getValue().equals(entity.getAdminUnit().getParent().getParent().getLevel())) {
                                    this.province = new AdministrativeUnitDto(entity.getAdminUnit().getParent().getParent(), false, false);
                                }
                            }
                        }
                    }
                }

                if (DatnConstants.AdministrativeUnitLevel.DISTRICT.getValue().equals(entity.getAdminUnit().getLevel())) {
                    this.district = new AdministrativeUnitDto(entity.getAdminUnit(), false, false);

                    // Kiểm tra và lấy thông tin Tỉnh (Province) của huyện
                    if (entity.getAdminUnit().getParent() != null &&
                            entity.getAdminUnit().getParent().getLevel() != null) {
                        if (DatnConstants.AdministrativeUnitLevel.PROVINCE.getValue().equals(entity.getAdminUnit().getParent().getLevel())) {
                            this.province = new AdministrativeUnitDto(entity.getAdminUnit().getParent(), false, false);
                        }
                    }
                }

                if (DatnConstants.AdministrativeUnitLevel.PROVINCE.getValue().equals(entity.getAdminUnit().getLevel())) {
                    this.province = new AdministrativeUnitDto(entity.getAdminUnit(), false, false);
                }
            }
        }
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
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

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }
}
