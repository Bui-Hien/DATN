package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.RecruitmentRequest;
import com.buihien.datn.domain.RecruitmentRequestItem;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Valid
public class RecruitmentRequestDto extends BaseObjectDto {
    @ValidEnumValue(enumClass = DatnConstants.RecruitmentRequestStatus.class, message = "Trạng thái yêu cầu tuyển dụng không hợp lệ")
    private Integer recruitmentRequestStatus; // trang thái: DatnConstants.RecruitmentRequestStatus
    private StaffDto proposer; // Người đề xuất
    private Date proposalDate; // Ngày đề xuất
    private StaffDto approver; // Người duyệt
    private Date approvalDate; // Thời gian duyệt
    private List<RecruitmentRequestItemDto> recruitmentRequestItems; // Các vị trí cần tuyển trong yêu cầu

    public RecruitmentRequestDto() {
    }

    public RecruitmentRequestDto(RecruitmentRequest entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.recruitmentRequestStatus = entity.getRecruitmentRequestStatus();
            this.proposer = new StaffDto(entity.getProposer(), false);
            this.proposalDate = entity.getProposalDate();
            this.approver = new StaffDto(entity.getApprover(), false);
            this.approvalDate = entity.getApprovalDate();
            if (isGetFull) {
                if (entity.getRecruitmentRequestItems() != null && !entity.getRecruitmentRequestItems().isEmpty()) {
                    this.recruitmentRequestItems = new ArrayList<>();
                    for (RecruitmentRequestItem item : entity.getRecruitmentRequestItems()) {
                        this.recruitmentRequestItems.add(new RecruitmentRequestItemDto(item, false));
                    }
                }
            }
        }
    }

    public Integer getRecruitmentRequestStatus() {
        return recruitmentRequestStatus;
    }

    public void setRecruitmentRequestStatus(Integer recruitmentRequestStatus) {
        this.recruitmentRequestStatus = recruitmentRequestStatus;
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

    public StaffDto getApprover() {
        return approver;
    }

    public void setApprover(StaffDto approver) {
        this.approver = approver;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public List<RecruitmentRequestItemDto> getRecruitmentRequestItems() {
        return recruitmentRequestItems;
    }

    public void setRecruitmentRequestItems(List<RecruitmentRequestItemDto> recruitmentRequestItems) {
        this.recruitmentRequestItems = recruitmentRequestItems;
    }
}