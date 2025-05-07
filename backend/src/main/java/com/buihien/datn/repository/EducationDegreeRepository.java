package com.buihien.datn.repository;

import com.buihien.datn.domain.EducationDegree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EducationDegreeRepository extends JpaRepository<EducationDegree, UUID> {
}
