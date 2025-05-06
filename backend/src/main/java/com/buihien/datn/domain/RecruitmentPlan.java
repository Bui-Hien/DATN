package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;

// ke hoach tuyen dung
@Table(name = "tbl_recruitment_plan")
@Entity
public class RecruitmentPlan extends BaseObject {
    @ManyToOne
    @JoinColumn(name = "recruitment_request_id")
    private RecruitmentRequest recruitmentRequest; // yeu cau

    @Column(name = "estimated_time_from")
    private Date estimatedTimeFrom; // thoi gian du kien tu

    @Column(name = "estimated_time_to")
    private Date estimatedTimeTo; // thoi gian du kien den

    public RecruitmentPlan() {
    }

    public RecruitmentRequest getRecruitmentRequest() {
        return recruitmentRequest;
    }

    public void setRecruitmentRequest(RecruitmentRequest recruitmentRequest) {
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
