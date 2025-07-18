package com.buihien.datn.repository;

import com.buihien.datn.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    @Query("SELECT d FROM Department d WHERE d.parent.id = :parentId")
    List<Department> findByParentId(@Param("parentId") UUID parentId);

}
