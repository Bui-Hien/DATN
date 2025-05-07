package com.buihien.datn.repository;

import com.buihien.datn.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT entity FROM Role entity WHERE entity.name = :name")
    Optional<Role> findByName(@Param("name") String name);
}
