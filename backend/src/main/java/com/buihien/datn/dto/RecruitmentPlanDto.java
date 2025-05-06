package com.buihien.datn.dto;

import com.buihien.datn.domain.RecruitmentPlan;

import java.util.Date;

public class RecruitmentPlanDto extends BaseObjectDto {
    private RecruitmentRequestDto recruitmentRequest; // yeu cau
    private Date estimatedTimeFrom; // thoi gian du kien tu
    private Date estimatedTimeTo; // thoi

    public RecruitmentPlanDto() {
    }

    public RecruitmentPlanDto(RecruitmentPlan entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.recruitmentRequest = new RecruitmentRequestDto(entity.getRecruitmentRequest(), false);
            this.estimatedTimeFrom = entity.getEstimatedTimeFrom();
            this.estimatedTimeTo = entity.getEstimatedTimeTo();
        }
    }

    public RecruitmentRequestDto getRecruitmentRequest() {
        return recruitmentRequest;
    }

    public void setRecruitmentRequest(RecruitmentRequestDto recruitmentRequest) {
        this.recruitmentRequest = recruitmentRequest;
    }

    public Date getEstimatedTimeFrom() {
        return estimatedTimeFrom;
    }

    public void setEstimatedTimeFrom(Date estimatedTimeFrom) {
        this.estimatedTimeFrom = estimatedTimeFrom;
    }

    public Date getEstimatedTimeTo() {
        return estimatedTimeTo;
    }

    public void setEstimatedTimeTo(Date estimatedTimeTo) {
        this.estimatedTimeTo = estimatedTimeTo;
    }
}
