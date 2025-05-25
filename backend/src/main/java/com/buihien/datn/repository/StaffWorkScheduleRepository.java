package com.buihien.datn.repository;

import com.buihien.datn.domain.StaffWorkSchedule;
import com.buihien.datn.dto.CalculatedWorkingDay.ShiftWorkTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface StaffWorkScheduleRepository extends JpaRepository<StaffWorkSchedule, UUID> {
    @Query("SELECT s FROM StaffWorkSchedule s WHERE s.workingDate BETWEEN :fromDate AND :toDate AND s.staff.id IN :staffIds")
    List<StaffWorkSchedule> findByWorkingDateBetweenAndStaffIds(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, @Param("staffIds") List<UUID> staffIds);

    //dếm só công được tính
    @Query("SELECT new com.buihien.datn.dto.CalculatedWorkingDay.ShiftWorkTypeCount(s.shiftWorkType, COUNT(s)) " +
            "FROM StaffWorkSchedule s " +
            "WHERE s.shiftWorkStatus = 4 " +
            "AND s.staff.id = :staffId " +
            "AND s.workingDate BETWEEN :startDate AND :endDate " +
            "GROUP BY s.shiftWorkType")
    List<ShiftWorkTypeCount> countShiftWorkTypeByStaffInDateRange(@Param("staffId") UUID staffId,
                                                                  @Param("startDate") Date startDate,
                                                                  @Param("endDate") Date endDate);

    @Query("SELECT s FROM StaffWorkSchedule s WHERE s.shiftWorkType = :shiftWorkType AND s.staff.id = :staffId AND s.workingDate BETWEEN :startOfDay AND :endOfDay")
    List<StaffWorkSchedule> findByShiftWorkTypeAndStaffAndWorkingDate(
            @Param("shiftWorkType") Integer shiftWorkType,
            @Param("staffId") UUID staffId,
            @Param("startOfDay") Date startOfDay,
            @Param("endOfDay") Date endOfDay
    );

    @Query("SELECT s FROM StaffWorkSchedule s WHERE s.staff.id = :staffId AND s.workingDate BETWEEN :startOfDay AND :endOfDay")
    List<StaffWorkSchedule> findStaffWorkScheduleByStaffAndWorkingDate(
            @Param("staffId") UUID staffId,
            @Param("startOfDay") Date startOfDay,
            @Param("endOfDay") Date endOfDay
    );
}
