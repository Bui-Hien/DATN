package com.buihien.datn.domain;

import jakarta.persistence.*;

@Table(name = "tbl_recruitment_request_item")
@Entity
public class RecruitmentRequestItem extends AuditableEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "recruitment_request_id")
    private RecruitmentRequest recruitmentRequest; // Thuộc yêu cầu tuyển dụng nào
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id")
    private Position position; //Vị trí cần tuyển
    @Column(name = "gender")
    private String gender;// Giới tính
    @Column(name = "year_of_experience")
    private Integer yearOfExperience;//Số năm kinh nghiệm liên quan đến vị trí tuyển:
    //Độ tuổi:
    @Column(name = "minimum_age")
    private Integer minimumAge;
    @Column(name = "maxium_age")
    private Integer maximumAge;
    //Thu nhập đề xuất
    @Column(name = "minium_income")
    private Double minimumIncome;
    @Column(name = "maximum_income")
    private Double maximumIncome;
    //Mô tả, yêu cầu công việc
    @Column(name = "description", columnDefinition = "MEDIUMTEXT")
    private String description; // mo ta cong viec
    @Column(name = "request", columnDefinition = "MEDIUMTEXT")
    private String request; // yeu cau

    public RecruitmentRequestItem() {
    }

    public RecruitmentRequest getRecruitmentRequest() {
        return recruitmentRequest;
    }

    public void setRecruitmentRequest(RecruitmentRequest recruitmentRequest) {
        this.recruitmentRequest = recruitmentRequest;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getYearOfExperience() {
        return yearOfExperience;
    }

    public void setYearOfExperience(Integer yearOfExperience) {
        this.yearOfExperience = yearOfExperience;
    }

    public Integer getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.minimumAge = minimumAge;
    }

    public Integer getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(Integer maximumAge) {
        this.maximumAge = maximumAge;
    }

    public Double getMinimumIncome() {
        return minimumIncome;
    }

    public void setMinimumIncome(Double minimumIncome) {
        this.minimumIncome = minimumIncome;
    }

    public Double getMaximumIncome() {
        return maximumIncome;
    }

    public void setMaximumIncome(Double maximumIncome) {
        this.maximumIncome = maximumIncome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
