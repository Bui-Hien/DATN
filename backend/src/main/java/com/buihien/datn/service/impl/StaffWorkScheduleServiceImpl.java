package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.Staff;
import com.buihien.datn.domain.StaffWorkSchedule;
import com.buihien.datn.dto.StaffWorkScheduleDto;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        if (entity == null) {
            entity = new StaffWorkSchedule();
        }
        if (entity.getIsLocked() != null && entity.getIsLocked()) {
            throw new InvalidDataException("Không thể sửa ca làm việc đã bị khóa");
        }
        entity.setShiftWorkType(dto.getShiftWorkType());
        Staff staff = null;
        if (dto.getStaff() != null && dto.getStaff().getId() != null) {
            staff = staffRepository.findById(dto.getStaff().getId()).orElse(null);
        }
        if (staff == null) {
            throw new ResourceNotFoundException("Nhân viên không tồn tại");
        }
        entity.setStaff(staff);
        entity.setWorkingDate(dto.getWorkingDate());
        Staff coordinator = null;
        if (dto.getCoordinator() != null && dto.getCoordinator().getId() != null) {
            coordinator = staffRepository.findById(dto.getCoordinator().getId()).orElse(null);
        }
        if (coordinator == null) {
            throw new ResourceNotFoundException("Người phân ca không tồn tại");
        }
        entity.setCoordinator(coordinator);
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
            whereClause.append(" AND (LOWER(entity.staff.displayName) LIKE LOWER(:text) ");
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
    public void markAttendance(UUID staffWorkScheduleId, boolean isCheckIn) {
        logger.info("Đang chấm công, {}", isCheckIn ? "check-in" : "check-out");

        // Kiểm tra ID ca làm việc không được để trống
        if (staffWorkScheduleId == null) {
            throw new InvalidDataException("Ca làm việc không được để trống");
        }

        // Tìm ca làm việc theo ID
        StaffWorkSchedule schedule = staffWorkScheduleRepository
                .findById(staffWorkScheduleId).orElseThrow(() -> {
                    logger.warn("Không tìm thấy ca làm việc với ID: {}", staffWorkScheduleId);
                    return new ResourceNotFoundException("Không tìm thấy ca làm việc");
                });

        // Kiểm tra quyền của người dùng
        Staff currentStaff = extractCurrentUserService.extractCurrentStaff();
        if (currentStaff == null) {
            throw new InvalidDataException("Người chấm công không hợp lệ");
        }
        boolean hasRole = extractCurrentUserService.hasAnyRole(
                List.of(DatnConstants.ROLE_ADMIN, DatnConstants.ROLE_SUPER_ADMIN, DatnConstants.ROLE_MANAGER)
        );
        if (hasRole || currentStaff.getId().equals(schedule.getStaff().getId())) {
            Date now = new Date(); // Lấy thời gian hiện tại của hệ thống
            // Kiểm tra nếu là admin, thì cho phép chấm công cho ngày trong quá khứ
            if (!hasRole) {
                // Kiểm tra xem thời gian hiện tại có nằm trong khoảng thời gian làm việc không
                if (schedule.getWorkingDate() != null && !DateTimeUtil.isDateBeforeOrEqual(schedule.getWorkingDate(), now)) {
                    throw new InvalidDataException("Thời gian chấm công không hợp lệ");
                }
            }

            // Kiểm tra xem thời gian hiện tại có nằm trong khoảng thời gian làm việc không
            if (schedule.getWorkingDate() != null && !DateTimeUtil.isSameDay(schedule.getWorkingDate(), now)) {
                throw new InvalidDataException("Thời gian chấm công không hợp lệ");
            }
            // Nếu ca làm việc đã bị khóa thì không cho phép chấm công
            if (Boolean.TRUE.equals(schedule.getIsLocked())) {
                throw new InvalidDataException("Ca làm việc đã bị khóa, không thể chấm công");
            }

            // Ghi nhận thời gian chấm công (vào hoặc ra)
            if (isCheckIn) {
                schedule.setCheckIn(now);
                logger.info("Đã chấm công giờ vào: {}", now);
            } else {
                schedule.setCheckOut(now);
                logger.info("Đã chấm công giờ ra: {}", now);
            }

            // Xác định trạng thái ca làm việc dựa trên giờ vào và ra
            if (schedule.getCheckIn() != null && schedule.getCheckOut() != null) {
                // Lấy thông tin loại ca làm việc
                DatnConstants.ShiftWorkType shiftWorkType = DatnConstants.ShiftWorkType.getShiftWorkType(schedule.getShiftWorkType());
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
                schedule.setShiftWorkStatus(DatnConstants.ShiftWorkStatus.NOT_STARTED.getValue());
            }

            // Lưu lại kết quả chấm công
            staffWorkScheduleRepository.save(schedule);
        } else {
            throw new InvalidDataException("Người chấm công không có quyền thực hiện thao tác này");
        }
    }


}
