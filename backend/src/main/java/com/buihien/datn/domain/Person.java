package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbl_person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person extends AuditableEntity {
    @Column(name = "first_name")
    protected String firstName; // Tên (Ví dụ: "Văn", "Thị")

    @Column(name = "last_name")
    protected String lastName; // Họ và tên đệm (Ví dụ: "Nguyễn", "Trần Văn")

    @Column(name = "display_name")
    protected String displayName; // Họ và tên đầy đủ để hiển thị

    @Column(name = "birth_date")
    protected Date birthDate; // Ngày sinh

    @Column(name = "birth_place")
    protected String birthPlace; // Nơi sinh

    @Column(name = "gender")
    protected Integer gender; // DatnConstants.Gender

    @Column(name = "phone_number")
    protected String phoneNumber; // Số điện thoại

    @Column(name = "id_number")
    protected String idNumber; // Số CMND/CCCD

    @Column(name = "id_number_issue_by")
    protected String idNumberIssueBy; // Nơi cấp CMND/CCCD

    @Column(name = "id_number_issue_date")
    protected Date idNumberIssueDate; // Ngày cấp CMND/CCCD

    @Column(name = "email")
    protected String email; // Địa chỉ email

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    protected Country nationality; // Quốc tịch (liên kết đến bảng Country)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ethnics_id")
    protected Ethnics ethnics; // Dân tộc (liên kết đến bảng Ethnics)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "religion_id")
    protected Religion religion; // Tôn giáo (liên kết đến bảng Religion)

    @Column(name = "marital_status")
    protected Integer maritalStatus; // DatnConstants.MaritalStatus

    @Column(name = "tax_code")
    private String taxCode; // Mã số thuế

    @OneToOne(fetch = FetchType.EAGER)
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "education_degree_id")
    protected EducationDegree educationDegree;// Trình độ học vấn

    @Column(name = "height")
    protected Double height; // Chiều cao (cm)

    @Column(name = "weight")
    protected Double weight; // Cân nặng (kg)

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<PersonFamilyRelationship> familyRelationships;// Quan hệ gia đình

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<PersonAddress> personAddresses;// Địa chỉ

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<Certificate> certificates; // Chứng chỉ

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<PersonBankAccount> personBankAccounts; // các tài khoản ngân hàng của nhân viên

    public Person() {
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

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public Ethnics getEthnics() {
        return ethnics;
    }

    public void setEthnics(Ethnics ethnics) {
        this.ethnics = ethnics;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EducationDegree getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(EducationDegree educationDegree) {
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

    public Set<PersonFamilyRelationship> getFamilyRelationships() {
        return familyRelationships;
    }

    public void setFamilyRelationships(Set<PersonFamilyRelationship> familyRelationships) {
        this.familyRelationships = familyRelationships;
    }

    public Set<PersonAddress> getPersonAddresses() {
        return personAddresses;
    }

    public void setPersonAddresses(Set<PersonAddress> personAddresses) {
        this.personAddresses = personAddresses;
    }

    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }

    public Set<PersonBankAccount> getPersonBankAccounts() {
        return personBankAccounts;
    }

    public void setPersonBankAccounts(Set<PersonBankAccount> personBankAccounts) {
        this.personBankAccounts = personBankAccounts;
    }
}