package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.*;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Valid
public class PersonDto extends AuditableDto {
    protected String firstName;
    protected String lastName;
    protected String displayName;
    protected Date birthDate;
    protected String birthPlace;
    @ValidEnumValue(enumClass = DatnConstants.Gender.class, message = "Giới tính không hợp lệ")
    protected Integer gender;
    protected String phoneNumber;
    protected String idNumber;
    protected String idNumberIssueBy;
    protected Date idNumberIssueDate;
    protected String email;
    protected CountryDto nationality;
    protected EthnicsDto ethnics;
    protected ReligionDto religion;
    @ValidEnumValue(enumClass = DatnConstants.MaritalStatus.class, message = "Tình trạng hôn nhân không hợp lệ")
    protected Integer maritalStatus;
    protected String taxCode;
    protected UserDto user;
    protected EducationDegreeDto educationDegree;
    protected Double height;
    protected Double weight;
    protected FileDescriptionDto avatar;
    protected PersonAddressDto permanentResidence; //thường chú
    protected PersonAddressDto temporaryResidence;// Tạm chú
    protected List<PersonFamilyRelationshipDto> familyRelationships;
    protected List<CertificateDto> certificates;
    protected List<PersonBankAccountDto> personBankAccounts;

    public PersonDto() {
    }

    public PersonDto(Person entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.firstName = entity.getFirstName();
            this.lastName = entity.getLastName();
            this.displayName = entity.getDisplayName();
            this.birthDate = entity.getBirthDate();
            this.birthPlace = entity.getBirthPlace();
            this.gender = entity.getGender();
            this.phoneNumber = entity.getPhoneNumber();
            this.idNumber = entity.getIdNumber();
            this.idNumberIssueBy = entity.getIdNumberIssueBy();
            this.idNumberIssueDate = entity.getIdNumberIssueDate();
            this.email = entity.getEmail();
            this.maritalStatus = entity.getMaritalStatus();
            this.taxCode = entity.getTaxCode();
            this.height = entity.getHeight();
            this.weight = entity.getWeight();

            if (entity.getAvatar() != null) {
                this.avatar = new FileDescriptionDto(entity.getAvatar());
            }
            if (entity.getNationality() != null) {
                this.nationality = new CountryDto(entity.getNationality());
            }

            if (entity.getEthnics() != null) {
                this.ethnics = new EthnicsDto(entity.getEthnics());
            }

            if (entity.getReligion() != null) {
                this.religion = new ReligionDto(entity.getReligion());
            }

            if (entity.getUser() != null && isGetFull) {
                this.user = new UserDto(entity.getUser(), false); // Tránh vòng lặp đệ quy
            }

            if (entity.getEducationDegree() != null) {
                this.educationDegree = new EducationDegreeDto(entity.getEducationDegree());
            }

            if (entity.getPermanentResidence() != null) {
                this.permanentResidence = new PersonAddressDto(entity.getPermanentResidence());
            }
            if (entity.getTemporaryResidence() != null) {
                this.temporaryResidence = new PersonAddressDto(entity.getTemporaryResidence());
            }
            if (isGetFull) {
                if (entity.getFamilyRelationships() != null && !entity.getFamilyRelationships().isEmpty()) {
                    this.familyRelationships = new ArrayList<>();
                    for (PersonFamilyRelationship personFamilyRelationship : entity.getFamilyRelationships()) {
                        PersonFamilyRelationshipDto dto = new PersonFamilyRelationshipDto(personFamilyRelationship, false);
                        this.familyRelationships.add(dto);
                    }
                }


                if (entity.getCertificates() != null && !entity.getCertificates().isEmpty()) {
                    this.certificates = new ArrayList<>();
                    for (Certificate certificate : entity.getCertificates()) {
                        CertificateDto dto = new CertificateDto(certificate, false);
                        this.certificates.add(dto);
                    }
                }
                if (entity.getPersonBankAccounts() != null && !entity.getPersonBankAccounts().isEmpty()) {
                    this.personBankAccounts = new ArrayList<>();
                    for (PersonBankAccount personBankAccount : entity.getPersonBankAccounts()) {
                        PersonBankAccountDto dto = new PersonBankAccountDto(personBankAccount, false);
                        this.personBankAccounts.add(dto);
                    }
                }
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdNumberIssueBy() {
        return idNumberIssueBy;
    }

    public void setIdNumberIssueBy(String idNumberIssueBy) {
        this.idNumberIssueBy = idNumberIssueBy;
    }

    public Date getIdNumberIssueDate() {
        return idNumberIssueDate;
    }

    public void setIdNumberIssueDate(Date idNumberIssueDate) {
        this.idNumberIssueDate = idNumberIssueDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CountryDto getNationality() {
        return nationality;
    }

    public void setNationality(CountryDto nationality) {
        this.nationality = nationality;
    }

    public EthnicsDto getEthnics() {
        return ethnics;
    }

    public void setEthnics(EthnicsDto ethnics) {
        this.ethnics = ethnics;
    }

    public ReligionDto getReligion() {
        return religion;
    }

    public void setReligion(ReligionDto religion) {
        this.religion = religion;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public EducationDegreeDto getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(EducationDegreeDto educationDegree) {
        this.educationDegree = educationDegree;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public FileDescriptionDto getAvatar() {
        return avatar;
    }

    public void setAvatar(FileDescriptionDto avatar) {
        this.avatar = avatar;
    }

    public List<PersonFamilyRelationshipDto> getFamilyRelationships() {
        return familyRelationships;
    }

    public void setFamilyRelationships(List<PersonFamilyRelationshipDto> familyRelationships) {
        this.familyRelationships = familyRelationships;
    }

    public PersonAddressDto getPermanentResidence() {
        return permanentResidence;
    }

    public void setPermanentResidence(PersonAddressDto permanentResidence) {
        this.permanentResidence = permanentResidence;
    }

    public PersonAddressDto getTemporaryResidence() {
        return temporaryResidence;
    }

    public void setTemporaryResidence(PersonAddressDto temporaryResidence) {
        this.temporaryResidence = temporaryResidence;
    }

    public List<CertificateDto> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateDto> certificates) {
        this.certificates = certificates;
    }

    public List<PersonBankAccountDto> getPersonBankAccounts() {
        return personBankAccounts;
    }

    public void setPersonBankAccounts(List<PersonBankAccountDto> personBankAccounts) {
        this.personBankAccounts = personBankAccounts;
    }
}
