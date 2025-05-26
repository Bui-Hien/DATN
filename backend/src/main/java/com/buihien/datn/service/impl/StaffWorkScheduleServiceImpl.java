package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.domain.StaffWorkSchedule;
import com.buihien.datn.dto.*;
import com.buihien.datn.dto.search.StaffWorkScheduleSearchDto;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.generic.GenericServiceImpl;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.repository.StaffWorkScheduleRepository;
import com.buihien.datn.service.ExtractCurrentUserService;
import com.buihien.datn.service.StaffWorkScheduleService;
import com.buihien.datn.util.DateTimeUtil;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.buihien.datn.util.DateTimeUtil.getEndOfDay;
import static com.buihien.datn.util.DateTimeUtil.getStartOfDay;

@Service
public class StaffWorkScheduleServiceImpl extends GenericServiceImpl<StaffWorkSchedule, StaffWorkScheduleDto, StaffWorkScheduleSearchDto> implements StaffWorkScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(StaffWorkScheduleServiceImpl.class);
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StaffWorkScheduleRepository staffWorkScheduleRepository;
    @Autowired
    private ExtractCurrentUserService extractCurrentUserService;

    @Override
    protected StaffWorkScheduleDto convertToDto(StaffWorkSchedule entity) {
        return new StaffWorkScheduleDto(entity, true);
    }

    @Override
    protected StaffWorkSchedule convertToEntity(StaffWorkScheduleDto dto) {
        StaffWorkSchedule entity = null;
        if (dto.getId() != null) {
            entity = repository.findById(dto.getId()).orElse(null);
        }
        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            staff = staffRepository.findById(dto.getStaff().getId()).orElse(null);
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }

        Date startOfDay = getStartOfDay(dto.getWorkingDate());
        Date endOfDay = getEndOfDay(dto.getWorkingDate());
        List<StaffWorkSchedule> schedules = staffWorkScheduleRepository
                .findByShiftWorkTypeAndStaffAndWorkingDate(dto.getShiftWorkType(), staff.getId(), startOfDay, endOfDay);
        if (schedules != null && !schedules.isEmpty()) {
            entity = schedules.get(0);
        }
        if (entity == null) {
            entity = new StaffWorkSchedule();
        }

        if (entity.getIsLocked() != null && entity.getIsLocked()) {
            throw new InvalidDataException("Không thể sửa ca làm việc đã bị khóa");
        }
        entity.setStaff(staff);
        entity.setShiftWorkType(dto.getShiftWorkType());
        entity.setWorkingDate(dto.getWorkingDate());
        Staff coordinator = extractCurrentUserService.extractCurrentStaff();
        entity.setCoordinator(coordinator);
        entity.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.CREATED.getValue());

        return entity;
    }

    @Override
    public Page<StaffWorkScheduleDto> pagingSearch(StaffWorkScheduleSearchDto dto) {
        int pageIndex = (dto.getPageIndex() == null || dto.getPageIndex() < 1) ? 0 : dto.getPageIndex() - 1;
        int pageSize = (dto.getPageSize() == null || dto.getPageSize() < 10) ? 10 : dto.getPageSize();

        boolean isExportExcel = dto.getExportExcel() != null && dto.getExportExcel();


        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(entity.id) FROM StaffWorkSchedule entity WHERE (1=1) ");
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.StaffWorkScheduleDto(entity, false) FROM StaffWorkSchedule entity WHERE (1=1) ");

        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.staff.displayName) LIKE LOWER(:text) OR LOWER(entity.staff.staffCode) LIKE LOWER(:text)");
        }
        if (dto.getOwnerId() != null) {
            whereClause.append(" AND entity.staff.id = :ownerId ");
        }
        if (dto.getShiftWorkStatus() != null) {
            whereClause.append(" AND entity.coordinator.id = :coordinatorId ");
        }
        if (dto.getShiftWorkType() != null) {
            whereClause.append(" AND entity.shiftWorkType = :shiftWorkType ");
        }
        if (dto.getShiftWorkStatus() != null) {
            whereClause.append(" AND entity.shiftWorkStatus = :shiftWorkStatus ");
        }
        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.workingDate >= :fromDate ");
        }
        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.workingDate <= :toDate ");
        }
        if (dto.getTimeSheetDetail() != null && dto.getTimeSheetDetail()) {
            whereClause.append(" AND entity.shiftWorkStatus != :shiftWorkStatus");
        }
        sql.append(whereClause);
        sqlCount.append(whereClause);

        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.workingDate ASC" : " ORDER BY entity.workingDate DESC");

        Query q = manager.createQuery(sql.toString(), StaffWorkScheduleDto.class);
        Query qCount = manager.createQuery(sqlCount.toString());

        if (dto.getKeyword() != null && StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", '%' + dto.getKeyword() + '%');
            qCount.setParameter("text", '%' + dto.getKeyword() + '%');
        }
        if (dto.getOwnerId() != null) {
            q.setParameter("ownerId", dto.getOwnerId());
            qCount.setParameter("ownerId", dto.getOwnerId());
        }
        if (dto.getShiftWorkStatus() != null) {
            q.setParameter("coordinatorId", dto.getShiftWorkStatus());
            qCount.setParameter("coordinatorId", dto.getShiftWorkStatus());
        }
        if (dto.getShiftWorkType() != null) {
            q.setParameter("shiftWorkType", dto.getShiftWorkType());
            qCount.setParameter("shiftWorkType", dto.getShiftWorkType());
        }
        if (dto.getShiftWorkStatus() != null) {
            q.setParameter("shiftWorkStatus", dto.getShiftWorkStatus());
            qCount.setParameter("shiftWorkStatus", dto.getShiftWorkStatus());
        }
        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", dto.getFromDate());
            qCount.setParameter("fromDate", dto.getFromDate());
        }
        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", dto.getFromDate());
            qCount.setParameter("fromDate", dto.getFromDate());
        }
        if (dto.getToDate() != null) {
            q.setParameter("toDate", dto.getToDate());
            qCount.setParameter("toDate", dto.getToDate());
        }
        if (dto.getTimeSheetDetail() != null && dto.getTimeSheetDetail()) {
            int shiftWorkStatus = DatnConstants.ShiftWorkStatus.CREATED.getValue();
            q.setParameter("shiftWorkStatus", shiftWorkStatus);
            qCount.setParameter("shiftWorkStatus", shiftWorkStatus);
        }

        if (!isExportExcel) {
            q.setFirstResult(pageIndex * pageSize);
            q.setMaxResults(pageSize);

            return new PageImpl<>(q.getResultList(), PageRequest.of(pageIndex, pageSize), (long) qCount.getSingleResult());
        }
        return new PageImpl<>(q.getResultList());
    }

    @Override
    public void lockListScheduleByFromDateToDateAndListStaffIds(Date fromDate, Date toDate, List<UUID> staffIds) {
        logger.info("Đang tiến hành khóa các lịch làm việc từ ngày {} đến ngày {} cho danh sách nhân viên: {}", fromDate, toDate, staffIds);

        // Kiểm tra dữ liệu đầu vào
        if (fromDate == null || toDate == null) {
            throw new InvalidDataException("Ngày bắt đầu hoặc ngày kết thúc không được để trống");
        }
        if (fromDate.after(toDate)) {
            throw new InvalidDataException("Ngày bắt đầu không được lớn hơn ngày kết thúc");
        }
        if (staffIds == null || staffIds.isEmpty()) {
            throw new InvalidDataException("Danh sách nhân viên không được để trống");
        }

        // Lấy danh sách ca làm việc trong khoảng thời gian và của các nhân viên chỉ định
        List<StaffWorkSchedule> schedules = staffWorkScheduleRepository
                .findByWorkingDateBetweenAndStaffIds(fromDate, toDate, staffIds);

        // Kiểm tra nếu không tìm thấy ca làm việc nào
        if (schedules.isEmpty()) {
            logger.warn("Không tìm thấy lịch làm việc nào trong khoảng thời gian và danh sách nhân viên đã chọn.");
            return;
        }

        // Đánh dấu là đã khóa và lưu lại
        List<StaffWorkSchedule> lockedSchedules = new ArrayList<>();
        for (StaffWorkSchedule schedule : schedules) {
            schedule.setIsLocked(true); // Đánh dấu là đã khóa
            lockedSchedules.add(schedule);
        }

        repository.saveAll(lockedSchedules);
        logger.info("Đã khóa thành công {} lịch làm việc.", schedules.size());
    }

    @Override
    public StaffWorkScheduleDto markAttendance(StaffWorkScheduleDto dto) {
        // Kiểm tra ID ca làm việc không được để trống
        if (dto == null || dto.getId() == null) {
            throw new InvalidDataException("Ca làm việc không được để trống");
        }

        // Tìm ca làm việc theo ID
        StaffWorkSchedule schedule = staffWorkScheduleRepository
                .findById(dto.getId()).orElseThrow(() -> {
                    logger.warn("Không tìm thấy ca làm việc với ID: {}", dto.getId());
                    return new ResourceNotFoundException("Không tìm thấy ca làm việc");
                });


        boolean hasRole = extractCurrentUserService.hasAnyRole(
                List.of(DatnConstants.ROLE_ADMIN, DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_MANAGER)
        );

        // Kiểm tra quyền của người dùng
        Staff currentStaff = extractCurrentUserService.extractCurrentStaff();
        if (currentStaff == null && !hasRole) {
            throw new InvalidDataException("Người chấm công không hợp lệ");
        }
        if (hasRole || currentStaff.getId().equals(schedule.getStaff().getId())) {
            Date now = new Date(); // Lấy thời gian hiện tại của hệ thống
            // Kiểm tra nếu là admin, thì cho phép chấm công cho ngày trong quá khứ
            if (!hasRole) {
                // Kiểm tra xem thời gian hiện tại có nằm trong khoảng thời gian làm việc không
                if (schedule.getWorkingDate() != null && !DateTimeUtil.isDateBeforeOrEqual(schedule.getWorkingDate(), now)) {
                    throw new InvalidDataException("Thời gian chấm công không hợp lệ");
                }
            }
            // Nếu ca làm việc đã bị khóa thì không cho phép chấm công
            if (Boolean.TRUE.equals(schedule.getIsLocked())) {
                throw new InvalidDataException("Ca làm việc đã bị khóa, không thể chấm công");
            }

            if (hasRole) {
                schedule.setCheckIn(dto.getCheckIn());
                schedule.setCheckOut(dto.getCheckOut());
            } else {
                if (schedule.getCheckIn() == null && schedule.getCheckOut() == null) {
                    schedule.setCheckIn(now);
                    logger.info("Đã chấm công giờ vào: {}", now);
                } else {
                    schedule.setCheckOut(now);
                    logger.info("Đã chấm công giờ ra: {}", now);
                }
            }

            // Xác định trạng thái ca làm việc dựa trên giờ vào và ra
            if (schedule.getCheckIn() != null && schedule.getCheckOut() != null) {
                // Lấy thông tin loại ca làm việc
                DatnConstants.ShiftWorkType shiftWorkType = DatnConstants.ShiftWorkType.getShiftWorkType(schedule.getShiftWorkType());
                if (shiftWorkType == null) {
                    throw new InvalidDataException("Ca làm việc không tồn tại");
                }

                Date expectedStart = shiftWorkType.getStartTime();
                Date expectedEnd = shiftWorkType.getEndTime();

                // Tính toán số giờ làm việc thực tế và yêu cầu
                long plannedDurationMillis = expectedEnd.getTime() - expectedStart.getTime();
                long actualDurationMillis = schedule.getCheckOut().getTime() - schedule.getCheckIn().getTime();

                double workedHours = actualDurationMillis / (1000.0 * 60 * 60);
                double requiredHours = plannedDurationMillis / (1000.0 * 60 * 60);

                boolean lateCheckIn = schedule.getCheckIn().after(expectedStart); // vào muộn
                boolean earlyCheckOut = schedule.getCheckOut().before(expectedEnd); // về sớm

                // Xác định trạng thái dựa trên các điều kiện
                if (workedHours < 0.5 * requiredHours) {
                    schedule.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.ABSENT.getValue()); // Làm ít hơn 50% => Nghỉ
                } else if (lateCheckIn || earlyCheckOut) {
                    schedule.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.INSUFFICIENT_HOURS.getValue()); // Đi muộn hoặc về sớm => Thiếu giờ
                } else {
                    schedule.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.WORKED_FULL_HOURS.getValue()); // Làm đủ giờ => Đủ giờ
                }

            } else if (schedule.getCheckIn() != null) {
                // Mới chỉ chấm công giờ vào
                schedule.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.CHECKED_IN.getValue());
            } else {
                // Chưa chấm công
                schedule.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.CREATED.getValue());
            }

            // Lưu lại kết quả chấm công
            schedule = staffWorkScheduleRepository.save(schedule);
            return new StaffWorkScheduleDto(schedule, true);
        } else {
            throw new InvalidDataException("Người chấm công không có quyền thực hiện thao tác này");
        }
    }

    @Override
    @Transactional
    public Integer saveList(StaffWorkScheduleListDto dto) {
        if (dto == null) {
            throw new InvalidDataException("Dữ liệu phân ca không được để trống");
        }

        if (dto.getStaffs() == null || dto.getStaffs().isEmpty()) {
            throw new InvalidDataException("Chưa chọn nhân viên để phân ca");
        }

        if (dto.getShiftWorkTypeList() == null || dto.getShiftWorkTypeList().isEmpty()) {
            throw new InvalidDataException("Chưa chọn loại ca để phân");
        }

        if (dto.getFromWorkingDate() == null) {
            throw new InvalidDataException("Ngày bắt đầu làm việc không được để trống");
        }

        if (dto.getToWorkingDate() == null) {
            throw new InvalidDataException("Ngày kết thúc làm việc không được để trống");
        }

        if (dto.getFromWorkingDate().after(dto.getToWorkingDate())) {
            throw new InvalidDataException("Ngày bắt đầu phải trước hoặc bằng ngày kết thúc");
        }

        if (dto.getWeekdays() == null || dto.getWeekdays().isEmpty()) {
            throw new InvalidDataException("Chưa chọn các ngày trong tuần áp dụng");
        }


        Date fromDate = dto.getFromWorkingDate();
        Date toDate = dto.getToWorkingDate();
        List<Integer> weekdays = dto.getWeekdays();
        List<Integer> shiftWorkTypeList = dto.getShiftWorkTypeList();
        List<StaffDto> staffDtos = dto.getStaffs();

        // Lặp qua từng ngày từ fromDate đến toDate
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fromDate);

        //Người phân ca
        Staff coordinator = extractCurrentUserService.extractCurrentStaff();

        List<StaffWorkSchedule> staffWorkScheduleList = new ArrayList<>();
        while (!calendar.getTime().after(toDate)) {
            Date currentDate = calendar.getTime();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int convertedDay = convertJavaDayOfWeek(dayOfWeek); // Convert Java Sunday=1 -> ISO Monday=1

            if (weekdays.contains(convertedDay)) {
                for (StaffDto staffDto : staffDtos) {
                    Staff staff = staffRepository.findById(staffDto.getId()).orElse(null);
                    if (staff == null) continue;

                    for (Integer shiftType : shiftWorkTypeList) {
                        StaffWorkSchedule schedule = null;
                        Date startOfDay = getStartOfDay(currentDate);
                        Date endOfDay = getEndOfDay(currentDate);

                        List<StaffWorkSchedule> schedules = staffWorkScheduleRepository
                                .findByShiftWorkTypeAndStaffAndWorkingDate(shiftType, staff.getId(), startOfDay, endOfDay);
                        if (schedules != null && !schedules.isEmpty()) {
                            schedule = schedules.get(0);
                        }
                        if (schedule == null) {
                            schedule = new StaffWorkSchedule();
                        }

                        schedule.setWorkingDate(currentDate);
                        schedule.setStaff(staff);
                        schedule.setShiftWorkType(shiftType);
                        schedule.setCoordinator(coordinator);
                        if (schedule.getShiftWorkStatus() == null) {
                            schedule.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.CREATED.getValue());
                        }
                        staffWorkScheduleList.add(schedule);
                    }
                }
            }

            calendar.add(Calendar.DATE, 1); // Sang ngày tiếp theo
        }
        staffWorkScheduleList = repository.saveAll(staffWorkScheduleList);
        return staffWorkScheduleList.size();
    }

    @Override
    public List<StaffWorkScheduleDto> getListByStaffAndWorkingDate(StaffWorkScheduleSearchDto dto) {
        if (dto == null || dto.getStaffId() == null) {
            throw new InvalidDataException("Chưa chọn nhân viên để chấm công");
        }

        if (dto.getWorkingDate() == null) {
            throw new InvalidDataException("Ngày làm việc không được để trống");
        }

        Date startOfDay = getStartOfDay(dto.getWorkingDate());
        Date endOfDay = getEndOfDay(dto.getWorkingDate());

        List<StaffWorkSchedule> schedules = staffWorkScheduleRepository
                .findStaffWorkScheduleByStaffAndWorkingDate(dto.getStaffId(), startOfDay, endOfDay);
        List<StaffWorkScheduleDto> staffWorkScheduleDtoList = new ArrayList<>();
        for (StaffWorkSchedule item : schedules) {
            staffWorkScheduleDtoList.add(new StaffWorkScheduleDto(item, true));
        }
        return staffWorkScheduleDtoList;
    }

    @Override
    public Page<StaffWorkScheduleSummaryDto> getScheduleSummary(StaffWorkScheduleSearchDto dto) {
        StringBuilder sql = new StringBuilder("SELECT new com.buihien.datn.dto.StaffWorkScheduleDto(entity, false) FROM StaffWorkSchedule entity WHERE (1=1) ");
        StringBuilder whereClause = new StringBuilder();

        if (dto.getVoided() == null || !dto.getVoided()) {
            whereClause.append(" AND (entity.voided = false OR entity.voided IS NULL) ");
        } else {
            whereClause.append(" AND entity.voided = true ");
        }

        if (StringUtils.hasText(dto.getKeyword())) {
            whereClause.append(" AND (LOWER(entity.staff.displayName) LIKE LOWER(:text) OR LOWER(entity.staff.staffCode) LIKE LOWER(:text)) ");
        }
        if (dto.getOwnerId() != null) {
            whereClause.append(" AND entity.staff.id = :ownerId ");
        }
        if (dto.getShiftWorkStatus() != null) {
            whereClause.append(" AND entity.coordinator.id = :coordinatorId ");
            whereClause.append(" AND entity.shiftWorkStatus = :shiftWorkStatus ");
        }
        if (dto.getShiftWorkType() != null) {
            whereClause.append(" AND entity.shiftWorkType = :shiftWorkType ");
        }
        if (dto.getFromDate() != null) {
            whereClause.append(" AND entity.workingDate >= :fromDate ");
        }
        if (dto.getToDate() != null) {
            whereClause.append(" AND entity.workingDate <= :toDate ");
        }
        if (dto.getTimeSheetDetail() != null && dto.getTimeSheetDetail()) {
            whereClause.append(" AND entity.shiftWorkStatus != :excludedShiftWorkStatus ");
        }

        sql.append(whereClause);
        sql.append(dto.getOrderBy() != null && dto.getOrderBy() ? " ORDER BY entity.workingDate ASC" : " ORDER BY entity.workingDate DESC");

        Query q = manager.createQuery(sql.toString(), StaffWorkScheduleDto.class);

        if (StringUtils.hasText(dto.getKeyword())) {
            q.setParameter("text", "%" + dto.getKeyword() + "%");
        }
        if (dto.getOwnerId() != null) {
            q.setParameter("ownerId", dto.getOwnerId());
        }
        if (dto.getShiftWorkStatus() != null) {
            q.setParameter("coordinatorId", dto.getShiftWorkStatus());
            q.setParameter("shiftWorkStatus", dto.getShiftWorkStatus());
        }
        if (dto.getShiftWorkType() != null) {
            q.setParameter("shiftWorkType", dto.getShiftWorkType());
        }
        if (dto.getFromDate() != null) {
            q.setParameter("fromDate", dto.getFromDate());
        }
        if (dto.getToDate() != null) {
            q.setParameter("toDate", dto.getToDate());
        }
        if (dto.getTimeSheetDetail() != null && dto.getTimeSheetDetail()) {
            q.setParameter("excludedShiftWorkStatus", DatnConstants.ShiftWorkStatus.CREATED.getValue());
        }

        List<StaffWorkScheduleDto> allSchedules = q.getResultList();
        List<StaffWorkScheduleSummaryDto> result = new ArrayList<>();

        // Bước 1: Gom nhóm theo staff
        Map<UUID, List<StaffWorkScheduleDto>> staffGrouped = allSchedules.stream()
                .collect(Collectors.groupingBy(s -> s.getStaff().getId()));

        for (Map.Entry<UUID, List<StaffWorkScheduleDto>> staffEntry : staffGrouped.entrySet()) {
            StaffWorkScheduleSummaryDto summaryDto = new StaffWorkScheduleSummaryDto();
            StaffDto staff = staffEntry.getValue().get(0).getStaff(); // Lấy thông tin staff
            summaryDto.setStaff(staff);

            // Bước 2: Gom nhóm theo workingDate (đã loại giờ/phút/giây)
            Map<Date, List<StaffWorkScheduleDto>> dateGrouped = staffEntry.getValue().stream()
                    .peek(item -> {
                        // Xóa thông tin staff
                        item.setStaff(null);

                        // Lấy thời điểm hiện tại (bắt đầu ngày hôm nay)
                        Date todayStart = getStartOfDay(new Date());
                        Date workingDate = getStartOfDay(item.getWorkingDate());

                        if (workingDate.after(todayStart)) {
                            // Nếu ngày làm việc là tương lai
                            item.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.NOT_YET_DUE.getValue());
                        } else if (workingDate.before(todayStart) && DatnConstants.ShiftWorkStatus.CREATED.getValue().equals(item.getShiftWorkStatus())) {
                            // Nếu ngày làm việc là quá khứ và đang ở trạng thái CREATED
                            item.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.ABSENT.getValue());
                        }
                    })
                    .collect(Collectors.groupingBy(
                            item -> getStartOfDay(item.getWorkingDate()),
                            TreeMap::new, // <== Dùng TreeMap để đảm bảo thứ tự tăng dần theo ngày
                            Collectors.toList()
                    ));

            summaryDto.setStaffWorkSchedules(dateGrouped);
            result.add(summaryDto);

        }

        // Paging thủ công
        int pageIndex = 0;
        if (dto.getPageIndex() != null && dto.getPageIndex() > 0) {
            pageIndex = dto.getPageIndex() - 1;
        }
        int pageSize = dto.getPageSize();
        int total = result.size();

        int fromIndex = Math.min(pageIndex * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<StaffWorkScheduleSummaryDto> pagedList = result.subList(fromIndex, toIndex);

        return new PageImpl<>(pagedList, PageRequest.of(pageIndex + 1, pageSize), total);
    }


    private int convertJavaDayOfWeek(int javaDayOfWeek) {
        return javaDayOfWeek == Calendar.SUNDAY ? 7 : javaDayOfWeek - 1;
    }

}
