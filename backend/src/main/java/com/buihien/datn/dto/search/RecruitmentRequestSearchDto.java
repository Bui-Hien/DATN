package com.buihien.datn.dto.search;

import java.util.UUID;

public class RecruitmentRequestSearchDto extends SearchDto {
    private UUID proposerId;
    private UUID positionId;

    public RecruitmentRequestSearchDto() {
    }

    public UUID getProposerId() {
        return proposerId;
    }

    public void setProposerId(UUID proposerId) {
        this.proposerId = proposerId;
    }

    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }
}
