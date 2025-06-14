package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.StaffWorkScheduleDto;
import com.buihien.datn.dto.StaffWorkScheduleListDto;
import com.buihien.datn.dto.StaffMonthScheduleCalendarDto;
import com.buihien.datn.dto.search.StaffWorkScheduleSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.StaffWorkScheduleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/staff-work-schedule")
@RestController
public class RestStaffWorkScheduleController extends GenericApi<StaffWorkScheduleDto, StaffWorkScheduleSearchDto> {
    private final Logger log = LoggerFactory.getLogger(RestStaffWorkScheduleController.class);

    public RestStaffWorkScheduleController(GenericService<StaffWorkScheduleDto, StaffWorkScheduleSearchDto> genericService) {
        super(StaffWorkScheduleDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @PostMapping("/save-list")
    public ResponseData<Integer> saveStaffWorkScheduleList(@Valid @RequestBody StaffWorkScheduleListDto dto) {
        Integer savedCount = ((StaffWorkScheduleService) genericService).saveList(dto);
        log.info("Successfully saved {} records.", savedCount);
        return new ResponseData<>(HttpStatus.OK.value(), "Successfully saved list", savedCount);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN, DatnConstants.ROLE_USER})
    @PostMapping("/mark-attendance")
    public ResponseData<StaffWorkScheduleDto> markAttendance(@Valid @RequestBody StaffWorkScheduleDto dto) {
        StaffWorkScheduleDto result = ((StaffWorkScheduleService) genericService).markAttendance(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Chấm công thành công", result);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN, DatnConstants.ROLE_USER})
    @PostMapping("/get-list-staff-work-schedule")
    public ResponseData<List<StaffWorkScheduleDto>> getListByStaffAndWorkingDate(@Valid @RequestBody StaffWorkScheduleSearchDto dto) {
        List<StaffWorkScheduleDto> response = ((StaffWorkScheduleService) genericService).getListByStaffAndWorkingDate(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Các ca làm việc trong ngày của nhân viên", response);
    }


    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN, DatnConstants.ROLE_USER})
    @PostMapping("/staff-month-schedule-calendar")
    public ResponseData<Page<StaffMonthScheduleCalendarDto>> getStaffMonthScheduleCalendar(@Valid @RequestBody StaffWorkScheduleSearchDto dto) {
        Page<StaffMonthScheduleCalendarDto> response = ((StaffWorkScheduleService) genericService).getStaffMonthScheduleCalendar(dto);
        return new ResponseData<>(HttpStatus.OK.value(), "Thống kê công của nhân viên theo search object", response);
    }
}
