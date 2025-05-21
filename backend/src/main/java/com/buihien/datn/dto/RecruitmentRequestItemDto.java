package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.RecruitmentRequestItem;
import com.buihien.datn.dto.validator.ValidEnumValue;

public class RecruitmentRequestItemDto extends AuditableDto {
    private RecruitmentRequestDto recruitmentRequest; // Thuộc yêu cầu tuyển dụng nào
    private PositionDto position; //Vị trí cần tuyển
    @ValidEnumValue(enumClass = DatnConstants.Gender.class, message = "Giới tính không hợp lệ")
    private String gender;// Giới tính
    private Integer yearOfExperience;//Số năm kinh nghiệm liên quan đến vị trí tuyển:
    //Độ tuổi:
    private Integer minimumAge;
    private Integer maximumAge;
    //Thu nhập đề xuất
    private Double minimumIncome;
    private Double maximumIncome;
    //Mô tả, yêu cầu công việc
    private String description; // mo ta cong viec
    private String request; // yeu cau

    public RecruitmentRequestItemDto() {
    }

    public RecruitmentRequestItemDto(RecruitmentRequestItem entity, Boolean isGetRecruitmentRequest) {
        super(entity);
        if (entity != null) {
            this.gender = entity.getGender();
            this.yearOfExperience = entity.getYearOfExperience();
            this.minimumAge = entity.getMinimumAge();
            this.maximumAge = entity.getMaximumAge();
            this.minimumIncome = entity.getMinimumIncome();
            this.maximumIncome = entity.getMaximumIncome();
            this.description = entity.getDescription();
            this.request = entity.getRequest();
            if (isGetRecruitmentRequest && entity.getRecruitmentRequest() != null) {
                this.recruitmentRequest = new RecruitmentRequestDto(entity.getRecruitmentRequest(), false);
            }
            if (entity.getPosition() != null) {
                this.position = new PositionDto(entity.getPosition(), true, true);
            }
        }
    }

    public RecruitmentRequestDto getRecruitmentRequest() {
        return recruitmentRequest;
    }

    public void setRecruitmentRequest(RecruitmentRequestDto recruitmentRequest) {
        this.recruitmentRequest = recruitmentRequest;
    }

    public PositionDto getPosition() {
        return position;
    }

    public void setPosition(PositionDto position) {
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
