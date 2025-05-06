package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;

// Kinh nghiệm làm việc của ứng viên ở các công ty/ tổ chức cũ
@Entity
@Table(name = "tbl_candidate_working_experience")
public class CandidateWorkingExperience extends AuditableEntity {
    /**
     * Ứng viên sở hữu kinh nghiệm làm việc này.
     */
    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

    /**
     * Tên công ty hoặc tổ chức mà ứng viên đã làm việc.
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * Ngày bắt đầu làm việc tại công ty.
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * Ngày kết thúc làm việc tại công ty.
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * Vị trí công việc mà ứng viên đảm nhiệm tại công ty đó.
     */
    @Column(name = "position")
    private String position;

    /**
     * Mức lương cuối cùng trước khi nghỉ việc.
     */
    private Double salary;

    /**
     * Lý do rời khỏi công ty.
     */
    @Column(name = "leaving_reason", columnDefinition = "TEXT")
    private String leavingReason;

    /**
     * Mô tả chi tiết về công việc đã làm.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    public CandidateWorkingExperience() {
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getLeavingReason() {
        return leavingReason;
    }

    public void setLeavingReason(String leavingReason) {
        this.leavingReason = leavingReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
