package com.buihien.datn.dto.search;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.UUID;

@Valid
public class StaffWorkScheduleSearchDto extends SearchDto {
    @ValidEnumValue(enumClass = DatnConstants.ShiftWorkType.class, message = "Loại ca làm việc không hợp lệ")
    private Integer shiftWorkType; // Loại ca làm việc. Chi tiết: DatnConstants.ShiftWorkType
    @ValidEnumValue(enumClass = DatnConstants.ShiftWorkStatus.class, message = "Trạng thái ca làm việc không hợp lệ")
    private Integer shiftWorkStatus; // Trạng thái ca làm việc. Chi tiết: DatnConstants.ShiftWorkStatus
    private UUID coordinatorId; // Người phân ca làm việc
    private UUID staffId;
    private Date workingDate;
    private Boolean timeSheetDetail;

    public StaffWorkScheduleSearchDto() {
    }

    public Integer getShiftWorkType() {
        return shiftWorkType;
    }

    public void setShiftWorkType(Integer shiftWorkType) {
        this.shiftWorkType = shiftWorkType;
    }

    public Integer getShiftWorkStatus() {
        return shiftWorkStatus;
    }

    public void setShiftWorkStatus(Integer shiftWorkStatus) {
        this.shiftWorkStatus = shiftWorkStatus;
    }

    public UUID getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(UUID coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public UUID getStaffId() {
        return staffId;
    }

    public void setStaffId(UUID staffId) {
        this.staffId = staffId;
    }

    public Date getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate) {
        this.workingDate = workingDate;
    }

    public Boolean getTimeSheetDetail() {
        return timeSheetDetail;
    }

    public void setTimeSheetDetail(Boolean timeSheetDetail) {
        this.timeSheetDetail = timeSheetDetail;
    }
}
