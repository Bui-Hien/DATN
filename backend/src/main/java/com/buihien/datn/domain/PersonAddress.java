package com.buihien.datn.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_person_address")
public class PersonAddress extends AuditableEntity {

    @Column(name = "address_detail")
    private String addressDetail; // Chi tiết địa chỉ (số nhà, tên đường,...)
    @ManyToOne
    @JoinColumn(name = "province_id")
    private AdministrativeUnit province;
    @ManyToOne
    @JoinColumn(name = "district_id")
    private AdministrativeUnit district;
    @ManyToOne
    @JoinColumn(name = "ward_id")
    private AdministrativeUnit ward;

    public PersonAddress() {
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public AdministrativeUnit getProvince() {
        return province;
    }

    public void setProvince(AdministrativeUnit province) {
        this.province = province;
    }

    public AdministrativeUnit getDistrict() {
        return district;
    }

    public void setDistrict(AdministrativeUnit district) {
        this.district = district;
    }

    public AdministrativeUnit getWard() {
        return ward;
    }

    public void setWard(AdministrativeUnit ward) {
        this.ward = ward;
    }
}
