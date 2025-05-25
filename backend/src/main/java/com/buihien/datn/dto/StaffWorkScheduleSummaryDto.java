package com.buihien.datn.dto;

import java.util.List;

//mỗi nhân viên trả về 1 list List<StaffWorkScheduleDto>
public class StaffWorkScheduleSummaryDto {
    StaffDto staff;
    List<StaffWorkScheduleDto> staffWorkSchedules;

    public StaffWorkScheduleSummaryDto() {
    }

    public StaffWorkScheduleSummaryDto(StaffDto staff, List<StaffWorkScheduleDto> staffWorkSchedules) {
        this.staff = staff;
        this.staffWorkSchedules = staffWorkSchedules;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public List<StaffWorkScheduleDto> getStaffWorkSchedules() {
        return staffWorkSchedules;
    }

    public void setStaffWorkSchedules(List<StaffWorkScheduleDto> staffWorkSchedules) {
        this.staffWorkSchedules = staffWorkSchedules;
    }
}
