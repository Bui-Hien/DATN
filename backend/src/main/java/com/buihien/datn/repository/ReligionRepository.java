package com.buihien.datn.repository;

import com.buihien.datn.domain.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReligionRepository extends JpaRepository<Religion, UUID> {
    @Query("SELECT r FROM Religion r WHERE r.code = :code")
    Religion findByCode(@Param("code") String code);
}
