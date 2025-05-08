package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

/*
 * ứng viên
 */
@Entity
@Table(name = "tbl_candidate")
public class Candidate extends Person {
    @Column(name = "candidate_code", unique = true)
    private String candidateCode; // ma ung vien

    @ManyToOne
    @JoinColumn(name = "recruitment_plan_id")
    private RecruitmentPlan recruitmentPlan; // kế hoạch tuyển dụng ứng viên

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position; // Vị trí ứng tuyển trong phòng ban ứng tuyển)

    @Column(name = "submission_date")
    private Date submissionDate; // Ngày nop ho so

    @Column(name = "interview_date")
    private Date interviewDate; // Ngày phong van
    @Column(name = "desired_pay")
    private Double desiredPay; // muc luong mong muon
    @Column(name = "possible_working_date")
    private Date possibleWorkingDate; // Ngày co the lam việc
    @Column(name = "onboard_date")
    private Date onboardDate; // Ngày ứng viên nhận việc

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "introducer_id")
    private Staff introducer; // Nhân viên giới thiệu ứng viên

    // tab 5 - Kinh nghiệm làm việc
    @OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CandidateWorkingExperience> candidateWorkingExperience; // Kinh nghiệm làm việc

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "candidate_status")
    private Integer candidateStatus; //Xem status: DatnConstants.CandidateStatus

    @OneToOne
    private FileDescription curriculumVitae; // cv của ứng viên

    public Candidate() {
    }

    public String getCandidateCode() {
        return candidateCode;
    }

    public void setCandidateCode(String candidateCode) {
        this.candidateCode = candidateCode;
    }

    public RecruitmentPlan getRecruitmentPlan() {
        return recruitmentPlan;
    }

    public void setRecruitmentPlan(RecruitmentPlan recruitmentPlan) {
        this.recruitmentPlan = recruitmentPlan;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(Date interviewDate) {
        this.interviewDate = interviewDate;
    }

    public Double getDesiredPay() {
        return desiredPay;
    }

    public void setDesiredPay(Double desiredPay) {
        this.desiredPay = desiredPay;
    }

    public Date getPossibleWorkingDate() {
        return possibleWorkingDate;
    }

    public void setPossibleWorkingDate(Date possibleWorkingDate) {
        this.possibleWorkingDate = possibleWorkingDate;
    }

    public Date getOnboardDate() {
        return onboardDate;
    }

    public void setOnboardDate(Date onboardDate) {
        this.onboardDate = onboardDate;
    }

    public Staff getIntroducer() {
        return introducer;
    }

    public void setIntroducer(Staff introducer) {
        this.introducer = introducer;
    }

    public Set<CandidateWorkingExperience> getCandidateWorkingExperience() {
        return candidateWorkingExperience;
    }

    public void setCandidateWorkingExperience(Set<CandidateWorkingExperience> candidateWorkingExperience) {
        this.candidateWorkingExperience = candidateWorkingExperience;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Integer getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(Integer candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public FileDescription getCurriculumVitae() {
        return curriculumVitae;
    }

    public void setCurriculumVitae(FileDescription curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }
}
