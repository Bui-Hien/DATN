package com.buihien.datn.service;

import com.buihien.datn.dto.StaffWorkScheduleDto;
import com.buihien.datn.dto.StaffWorkScheduleListDto;
import com.buihien.datn.dto.search.StaffWorkScheduleSearchDto;
import com.buihien.datn.generic.GenericService;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface StaffWorkScheduleService extends GenericService<StaffWorkScheduleDto, StaffWorkScheduleSearchDto> {
    void lockListScheduleByFromDateToDateAndListStaffIds(Date fromDate, Date toDate, List<UUID> staffIds);

    void markAttendance(UUID staffWorkScheduleId, boolean isCheckIn);

    Integer saveList(StaffWorkScheduleListDto dto);
}
