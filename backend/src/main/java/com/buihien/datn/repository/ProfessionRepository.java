package com.buihien.datn.repository;

import com.buihien.datn.domain.Profession;
import com.buihien.datn.domain.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, UUID> {
    @Query("SELECT p FROM Profession p WHERE p.code = :code")
    Profession findByCode(@Param("code") String code);
}
