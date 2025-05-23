package com.buihien.datn.repository;

import com.buihien.datn.domain.SalaryResultItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalaryResultItemRepository extends JpaRepository<SalaryResultItem, UUID> {
    @Query("SELECT entity FROM SalaryResultItem entity WHERE entity.staff.id = :staffId AND entity.salaryResult.id = :salaryResultId")
    SalaryResultItem findSalaryResultItemByStaffIdAndSalaryResultId(@Param("staffId") UUID staffId, @Param("salaryResultId") UUID salaryResultId);
}
