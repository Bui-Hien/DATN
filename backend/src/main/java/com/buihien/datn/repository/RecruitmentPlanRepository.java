package com.buihien.datn.repository;

import com.buihien.datn.domain.RecruitmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecruitmentPlanRepository extends JpaRepository<RecruitmentPlan, UUID> {
}
