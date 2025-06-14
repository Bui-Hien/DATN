package com.buihien.datn.dto.search;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.UUID;

@Valid
public class CandidateSearchDto extends SearchDto {
    private UUID recruitmentRequestId;//
    private UUID positionId;// Vị trí ứng tuyển trong phòng ban ứng tuyển)
    private UUID introducerId;// Nhân viên giới thiệu ứng viên
    @ValidEnumValue(enumClass = DatnConstants.CandidateStatus.class, message = "Trạng thái ứng viên không hợp lệ")
    private Integer candidateStatus; //Xem status: DatnConstants.CandidateStatus

    public CandidateSearchDto() {
    }

    public UUID getRecruitmentRequestId() {
        return recruitmentRequestId;
    }

    public void setRecruitmentRequestId(UUID recruitmentRequestId) {
        this.recruitmentRequestId = recruitmentRequestId;
    }

    public UUID getPositionId() {
        return positionId;
    }

    public void setPositionId(UUID positionId) {
        this.positionId = positionId;
    }

    public UUID getIntroducerId() {
        return introducerId;
    }

    public void setIntroducerId(UUID introducerId) {
        this.introducerId = introducerId;
    }

    public Integer getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(Integer candidateStatus) {
        this.candidateStatus = candidateStatus;
    }
}
