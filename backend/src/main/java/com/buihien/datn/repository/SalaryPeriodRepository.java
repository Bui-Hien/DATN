package com.buihien.datn.repository;

import com.buihien.datn.domain.SalaryPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SalaryPeriodRepository extends JpaRepository<SalaryPeriod, UUID> {
}
