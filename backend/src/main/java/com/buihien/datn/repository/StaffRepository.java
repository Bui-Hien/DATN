package com.buihien.datn.repository;

import com.buihien.datn.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    @Query("SELECT s FROM Staff s WHERE s.user.id = :userId")
    Staff findByUserId(UUID userId); // Truy vấn staff dựa trên userId
}
