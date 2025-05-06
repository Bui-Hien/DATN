package com.buihien.datn.dto;

import com.buihien.datn.domain.PersonFamilyRelationship;

import java.util.Date;

public class PersonFamilyRelationshipDto extends AuditableDto {
    protected PersonDto person;
    protected FamilyRelationshipDto familyRelationship;
    protected String fullName;
    protected PersonAddressDto personAddress;
    protected Date birthDate;
    protected ProfessionDto profession;

    public PersonFamilyRelationshipDto() {
    }

    public PersonFamilyRelationshipDto(PersonFamilyRelationship entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.fullName = entity.getFullName();
            this.birthDate = entity.getBirthDate();
            this.person = isGetFull && entity.getPerson() != null ? new PersonDto(entity.getPerson(), false) : null;
            this.familyRelationship = entity.getFamilyRelationship() != null ? new FamilyRelationshipDto(entity.getFamilyRelationship()) : null;
            this.personAddress = entity.getPersonAddress() != null ? new PersonAddressDto(entity.getPersonAddress(), false) : null;
            this.profession = entity.getProfession() != null ? new ProfessionDto(entity.getProfession()) : null;
        }
    }

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public FamilyRelationshipDto getFamilyRelationship() {
        return familyRelationship;
    }

    public void setFamilyRelationship(FamilyRelationshipDto familyRelationship) {
        this.familyRelationship = familyRelationship;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public PersonAddressDto getPersonAddress() {
        return personAddress;
    }

    public void setPersonAddress(PersonAddressDto personAddress) {
        this.personAddress = personAddress;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public ProfessionDto getProfession() {
        return profession;
    }

    public void setProfession(ProfessionDto profession) {
        this.profession = profession;
    }
}
