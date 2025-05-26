package com.buihien.datn.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StaffWorkScheduleSummaryDto {
    StaffDto staff;
    private Map<Date, List<StaffWorkScheduleDto>> staffWorkSchedules;

    public StaffWorkScheduleSummaryDto() {
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public Map<Date, List<StaffWorkScheduleDto>> getStaffWorkSchedules() {
        return staffWorkSchedules;
    }

    public void setStaffWorkSchedules(Map<Date, List<StaffWorkScheduleDto>> staffWorkSchedules) {
        this.staffWorkSchedules = staffWorkSchedules;
    }
}
