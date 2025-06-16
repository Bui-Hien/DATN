package com.buihien.datn.service;

import com.buihien.datn.dto.StaffMonthScheduleCalendarDto;
import com.buihien.datn.dto.StaffWorkScheduleDto;
import com.buihien.datn.dto.StaffWorkScheduleListDto;
import com.buihien.datn.dto.search.StaffWorkScheduleSearchDto;
import com.buihien.datn.generic.GenericService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface StaffWorkScheduleService extends GenericService<StaffWorkScheduleDto, StaffWorkScheduleSearchDto> {
    StaffWorkScheduleDto markAttendance(StaffWorkScheduleDto dto);

    Integer saveList(StaffWorkScheduleListDto dto);

    List<StaffWorkScheduleDto> getListByStaffAndWorkingDate(StaffWorkScheduleSearchDto dto);

    Page<StaffMonthScheduleCalendarDto> getStaffMonthScheduleCalendar(StaffWorkScheduleSearchDto dto);

    Boolean deleteMarkAttendanceById(UUID id);

    int deleteMultipleMarkAttendance(List<UUID> ids);

    StaffWorkScheduleDto getByStaffAndWorkingDateAndShiftWorkType(StaffWorkScheduleSearchDto dto);

}
