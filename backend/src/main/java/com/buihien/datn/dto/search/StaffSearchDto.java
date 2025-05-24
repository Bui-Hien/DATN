package com.buihien.datn.dto.search;

import com.buihien.datn.util.DateTimeUtil;

import java.util.Date;
import java.util.UUID;

public class StaffSearchDto extends SearchDto {
    private Date fromRecruitmentDate;//ngày bắt đầu tuyển dụng
    private Date toRecruitmentDate;//ngày kết thúc tuyển dụng
    private Date fromStartDate; // Ngày bắt đầu chính thức
    private Date toStartDate; // Ngày bắt đầu chính thức
    private Integer employeeStatus;//trạng thái nhân viên  //DatnConstants.EmployeeStatus
    private Integer staffPhase; // Loại nhân viên. Chi tiết: DatnConstants.StaffPhase
    protected Integer gender;//Giới tính DatnConstants.Gender
    protected Integer maritalStatus;//Tình trạng hôn nhân DatnConstants.MaritalStatus
    private UUID departmentId;//Phòng ban làm việc
    private Integer educationLevel;

    public StaffSearchDto() {
    }

    public Date getFromRecruitmentDate() {
        if (fromRecruitmentDate == null) {
            return null;
        }
        return DateTimeUtil.getStartOfDay(fromRecruitmentDate);
    }

    public void setFromRecruitmentDate(Date fromRecruitmentDate) {
        this.fromRecruitmentDate = fromRecruitmentDate;
    }

    public Date getToRecruitmentDate() {
        if (toRecruitmentDate == null) {
            return null;
        }
        return DateTimeUtil.getEndOfDay(toRecruitmentDate);
    }

    public void setToRecruitmentDate(Date toRecruitmentDate) {
        this.toRecruitmentDate = toRecruitmentDate;
    }

    public Date getFromStartDate() {
        if (fromStartDate == null) {
            return null;
        }
        return DateTimeUtil.getStartOfDay(fromStartDate);
    }

    public void setFromStartDate(Date fromStartDate) {
        this.fromStartDate = fromStartDate;
    }

    public Date getToStartDate() {
        if (toStartDate == null) {
            return null;
        }
        return DateTimeUtil.getEndOfDay(toStartDate);
    }

    public void setToStartDate(Date toStartDate) {
        this.toStartDate = toStartDate;
    }

    public Integer getEmployeeStatus() {
        return employeeStatus;
    }

    public void setEmployeeStatus(Integer employeeStatus) {
        this.employeeStatus = employeeStatus;
    }

    public Integer getStaffPhase() {
        return staffPhase;
    }

    public void setStaffPhase(Integer staffPhase) {
        this.staffPhase = staffPhase;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Integer maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(Integer educationLevel) {
        this.educationLevel = educationLevel;
    }
}
