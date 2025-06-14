package com.buihien.datn.dto.candidateupdatestatus;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.validator.ValidEnumValue;

import java.util.List;
import java.util.UUID;

public class CandidateStatusDto {
    private List<UUID> candidates;
    @ValidEnumValue(enumClass = DatnConstants.CandidateStatus.class, message = "Trạng thái ứng viên không hợp lệ")
    private Integer candidateStatus; //Xem status: DatnConstants.CandidateStatus

    public CandidateStatusDto() {
    }

    public List<UUID> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<UUID> candidates) {
        this.candidates = candidates;
    }

    public Integer getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(Integer candidateStatus) {
        this.candidateStatus = candidateStatus;
    }
}
