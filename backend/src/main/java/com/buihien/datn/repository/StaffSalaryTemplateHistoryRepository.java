package com.buihien.datn.repository;

import com.buihien.datn.domain.StaffSalaryTemplateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffSalaryTemplateHistoryRepository extends JpaRepository<StaffSalaryTemplateHistory, UUID> {
    @Query("SELECT entity FROM StaffSalaryTemplateHistory entity WHERE entity.staff.id = :staffId")
    List<StaffSalaryTemplateHistory> findByStaffId(@Param("staffId") UUID staffId);

    @Query("SELECT entity FROM StaffSalaryTemplateHistory entity WHERE entity.staff.id = :staffId AND entity.isCurrent = true")
    List<StaffSalaryTemplateHistory> findStaffSalaryTemplateHistoryByStaffIdAndIsCurrent(@Param("staffId") UUID staffId);
}
