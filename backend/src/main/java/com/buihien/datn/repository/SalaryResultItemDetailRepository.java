package com.buihien.datn.repository;

import com.buihien.datn.domain.SalaryResultItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalaryResultItemDetailRepository extends JpaRepository<SalaryResultItemDetail, UUID> {
    @Query("SELECT entity FROM SalaryResultItemDetail entity " +
            "WHERE entity.salaryResultItem.id = :salaryResultItemId and entity.salaryTemplateItem.id = :salaryTemplateItemId")
    SalaryResultItemDetail findBySalaryResultItemIdAndSalaryTemplateItemId(@Param("salaryResultItemId") UUID salaryResultItemId, @Param("salaryTemplateItemId") UUID salaryTemplateItemId);
}
