package com.buihien.datn.dto;

import com.buihien.datn.domain.RecruitmentRequest;
import jakarta.validation.Valid;

import java.util.Date;

@Valid
public class RecruitmentRequestDto extends BaseObjectDto {
    private StaffDto proposer; // Người đề xuất
    private Date proposalDate; // Ngày đề xuất

    public RecruitmentRequestDto() {
    }

    public RecruitmentRequestDto(RecruitmentRequest entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.proposer = new StaffDto(entity.getProposer(), false);
            this.proposalDate = entity.getProposalDate();

            if (isGetFull) {

            }
        }
    }

    public StaffDto getProposer() {
        return proposer;
    }

    public void setProposer(StaffDto proposer) {
        this.proposer = proposer;
    }

    public Date getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(Date proposalDate) {
        this.proposalDate = proposalDate;
    }
}