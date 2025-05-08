package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.Candidate;
import com.buihien.datn.domain.CandidateWorkingExperience;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Valid
public class CandidateDto extends PersonDto {
    private String candidateCode; // ma ung vien
    private RecruitmentPlanDto recruitmentPlan;
    private PositionDto position; // Vị trí ứng tuyển trong phòng ban ứng tuyển)
    private Date submissionDate; // Ngày nop ho so
    private Date interviewDate; // Ngày phong van
    private Double desiredPay; // muc luong mong muon
    private Date possibleWorkingDate; // Ngày co the lam việc
    private Date onboardDate; // Ngày ứng viên nhận việc
    private StaffDto introducer; // Nhân viên giới thiệu ứng viên
    private List<CandidateWorkingExperienceDto> candidateWorkingExperience; // Kinh nghiệm làm việc
    private StaffDto staff;
    @ValidEnumValue(enumClass = DatnConstants.CandidateStatus.class, message = "Trạng thái ứng viên không hợp lệ")
    private Integer candidateStatus; //Xem status: DatnConstants.CandidateStatus
    private FileDescriptionDto curriculumVitae; // cv của ứng viên

    public CandidateDto() {
        super();
    }

    public CandidateDto(Candidate entity, Boolean isGetFull) {
        super(entity, isGetFull);
        if (entity != null) {
            this.candidateCode = entity.getCandidateCode();
            this.recruitmentPlan = new RecruitmentPlanDto(entity.getRecruitmentPlan(), false);
            this.position = new PositionDto(entity.getPosition(), false);
            this.submissionDate = entity.getSubmissionDate();
            this.interviewDate = entity.getInterviewDate();
            this.desiredPay = entity.getDesiredPay();
            this.possibleWorkingDate = entity.getPossibleWorkingDate();
            this.onboardDate = entity.getOnboardDate();
            this.introducer = new StaffDto(entity.getIntroducer(), false);
            this.staff = new StaffDto(entity.getStaff(), false);
            this.candidateStatus = entity.getCandidateStatus();
            if (entity.getCurriculumVitae() != null) {
                this.curriculumVitae = new FileDescriptionDto(entity.getCurriculumVitae());
            }
            if (isGetFull) {
                if (entity.getCandidateWorkingExperience() != null && !entity.getCandidateWorkingExperience().isEmpty()) {
                    this.candidateWorkingExperience = new ArrayList<>();
                    for (CandidateWorkingExperience dto : entity.getCandidateWorkingExperience()) {
                        CandidateWorkingExperienceDto candidateWorkingExperienceDto = new CandidateWorkingExperienceDto(dto, false);
                        this.candidateWorkingExperience.add(candidateWorkingExperienceDto);
                    }
                }
            }
        }
    }

    public String getCandidateCode() {
        return candidateCode;
    }

    public void setCandidateCode(String candidateCode) {
        this.candidateCode = candidateCode;
    }

    public RecruitmentPlanDto getRecruitmentPlan() {
        return recruitmentPlan;
    }

    public void setRecruitmentPlan(RecruitmentPlanDto recruitmentPlan) {
        this.recruitmentPlan = recruitmentPlan;
    }

    public PositionDto getPosition() {
        return position;
    }

    public void setPosition(PositionDto position) {
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

    public StaffDto getIntroducer() {
        return introducer;
    }

    public void setIntroducer(StaffDto introducer) {
        this.introducer = introducer;
    }

    public List<CandidateWorkingExperienceDto> getCandidateWorkingExperience() {
        return candidateWorkingExperience;
    }

    public void setCandidateWorkingExperience(List<CandidateWorkingExperienceDto> candidateWorkingExperience) {
        this.candidateWorkingExperience = candidateWorkingExperience;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public Integer getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(Integer candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public FileDescriptionDto getCurriculumVitae() {
        return curriculumVitae;
    }

    public void setCurriculumVitae(FileDescriptionDto curriculumVitae) {
        this.curriculumVitae = curriculumVitae;
    }
}
