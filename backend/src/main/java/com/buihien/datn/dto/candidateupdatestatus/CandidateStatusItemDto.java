package com.buihien.datn.dto.candidateupdatestatus;

import java.util.UUID;

public class CandidateStatusItemDto {
    private UUID id;
    private String workExperience; // kinh nghiem của ứng viên
    private String request; // yeu cau
    private Boolean isPass;

    public CandidateStatusItemDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean pass) {
        isPass = pass;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
