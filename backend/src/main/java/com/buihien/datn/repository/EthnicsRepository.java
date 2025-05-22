package com.buihien.datn.repository;

import com.buihien.datn.domain.Country;
import com.buihien.datn.domain.Ethnics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EthnicsRepository extends JpaRepository<Ethnics, UUID> {
    @Query("SELECT e FROM Ethnics e WHERE e.code = :code")
    Ethnics findByCode(@Param("code") String code);
}
