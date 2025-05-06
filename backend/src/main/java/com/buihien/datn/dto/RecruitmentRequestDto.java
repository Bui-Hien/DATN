package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.Position;
import com.buihien.datn.domain.RecruitmentRequest;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.Set;

@Valid
public class RecruitmentRequestDto extends BaseObjectDto {
    @ValidEnumValue(enumClass = DatnConstants.RecruitmentRequestStatus.class, message = "Trạng thái yêu cầu tuyển dụng không hợp lệ")
    private Integer recruitmentRequestStatus; // trang thái: DatnConstants.RecruitmentRequestStatus
    private StaffDto proposer; // Người đề xuất
    private Date proposalDate; // Ngày đề xuất
    private StaffDto approver; // Người duyệt
    private Date approvalDate; // Thời gian duyệt
    private Set<PositionDto> positions; // Các vị trí tuyển dụng

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
                if (entity.getPositions() != null && !entity.getPositions().isEmpty()) {
                    for (Position dto : entity.getPositions()) {
                        PositionDto positionDto = new PositionDto(dto, false);
                        this.positions.add(positionDto);
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

    public Set<PositionDto> getPositions() {
        return positions;
    }

    public void setPositions(Set<PositionDto> positions) {
        this.positions = positions;
    }
}