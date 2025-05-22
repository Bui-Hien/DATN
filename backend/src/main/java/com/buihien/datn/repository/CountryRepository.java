package com.buihien.datn.repository;

import com.buihien.datn.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    @Query("SELECT c FROM Country c WHERE c.code = :code")
    Country findByCode(@Param("code") String code);
}
