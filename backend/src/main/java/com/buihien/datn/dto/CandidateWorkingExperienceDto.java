package com.buihien.datn.dto;

import com.buihien.datn.domain.CandidateWorkingExperience;

import java.util.Date;

public class CandidateWorkingExperienceDto extends AuditableDto {
    private CandidateDto candidate;
    private String companyName;
    private Date startDate;
    private Date endDate;
    private String position;
    private Double salary;
    private String leavingReason;
    private String description;

    public CandidateWorkingExperienceDto() {
    }

    public CandidateWorkingExperienceDto(CandidateWorkingExperience entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.companyName = entity.getCompanyName();
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.position = entity.getPosition();
            this.salary = entity.getSalary();
            this.leavingReason = entity.getLeavingReason();
            this.description = entity.getDescription();
            if (entity.getCandidate() != null) {
                this.candidate = new CandidateDto(entity.getCandidate(), false);
            }
            if (isGetFull) {
                //todo
            }
        }
    }

    public CandidateDto getCandidate() {
        return candidate;
    }

    public void setCandidate(CandidateDto candidate) {
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
