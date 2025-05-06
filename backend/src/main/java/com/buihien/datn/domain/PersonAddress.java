package com.buihien.datn.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_person_address")
public class PersonAddress extends AuditableEntity {

    @Column(name = "address_type")
    private Integer addressType; // DatnConstants.AddressType

    @Column(name = "address_detail")
    private String addressDetail; // Chi tiết địa chỉ (số nhà, tên đường,...)

    @ManyToOne
    @JoinColumn(name = "admin_unit_id")
    private AdministrativeUnit adminUnit; // Đơn vị hành chính tương ứng

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person; // Người sở hữu địa chỉ này

    public PersonAddress() {
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

    public AdministrativeUnit getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdministrativeUnit adminUnit) {
        this.adminUnit = adminUnit;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
