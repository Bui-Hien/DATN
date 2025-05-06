package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

// yeu cau tuyen dung
@Table(name = "tbl_recruitment_request")
@Entity
public class RecruitmentRequest extends BaseObject {
    private Integer recruitmentRequestStatus; // trang thái: DatnConstants.RecruitmentRequestStatus

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposer_id")
    private Staff proposer; // Người đề xuất

    @Column(name = "proposal_date")
    private Date proposalDate; // Ngày đề xuất

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Staff approver; // Người duyệt

    @Column(name = "approval_date")
    private Date approvalDate; // Thời gian duyệt

    @OneToMany(mappedBy = "recruitmentRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Position> positions; // Các vị trí tuyển dụng

    public RecruitmentRequest() {
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Staff getApprover() {
        return approver;
    }

    public void setApprover(Staff approver) {
        this.approver = approver;
    }

    public Date getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(Date proposalDate) {
        this.proposalDate = proposalDate;
    }

    public Staff getProposer() {
        return proposer;
    }

    public void setProposer(Staff proposer) {
        this.proposer = proposer;
    }

    public Integer getRecruitmentRequestStatus() {
        return recruitmentRequestStatus;
    }

    public void setRecruitmentRequestStatus(Integer recruitmentRequestStatus) {
        this.recruitmentRequestStatus = recruitmentRequestStatus;
    }
}
