package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;

@Table(name = "tbl_person_family_relationship")
@Entity
public class PersonFamilyRelationship extends AuditableEntity {

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    // Người mà mối quan hệ gia đình này thuộc về (liên kết đến bảng Person)

    @ManyToOne
    @JoinColumn(name = "family_relationship_id")
    private FamilyRelationship familyRelationship;
    // Mối quan hệ gia đình (Ví dụ: Cha, Mẹ, Anh, Chị, Em... liên kết đến bảng FamilyRelationship)

    @Column(name = "full_name")
    private String fullName;
    // Họ và tên đầy đủ của người thân

    @ManyToOne
    @JoinColumn(name = "person_address_id")
    private PersonAddress personAddress;
    // Địa chỉ của người thân (liên kết đến bảng PersonAddress)

    @Column(name = "birth_date")
    private Date birthDate;
    // Ngày sinh của người thân

    @ManyToOne
    @JoinColumn(name = "profession_id")
    private Profession profession;
    // Nghề nghiệp của người thân (liên kết đến bảng Profession)

    public PersonFamilyRelationship() {
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public FamilyRelationship getFamilyRelationship() {
        return familyRelationship;
    }

    public void setFamilyRelationship(FamilyRelationship familyRelationship) {
        this.familyRelationship = familyRelationship;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public PersonAddress getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(PersonAddress personAddress) {
        this.personAddress = personAddress;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }
}
