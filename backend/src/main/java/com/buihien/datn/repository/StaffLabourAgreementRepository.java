package com.buihien.datn.repository;

import com.buihien.datn.domain.StaffLabourAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffLabourAgreementRepository extends JpaRepository<StaffLabourAgreement, UUID> {
    @Query("SELECT s FROM StaffLabourAgreement s WHERE s.staff.id = :staffId AND s.agreementStatus = :status")
    List<StaffLabourAgreement> findByStaffIdAndAgreementStatus(@Param("staffId") UUID staffId, @Param("status") Integer status);

}
