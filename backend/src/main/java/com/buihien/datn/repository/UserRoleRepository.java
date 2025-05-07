package com.buihien.datn.repository;

import com.buihien.datn.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    @Query("SELECT entity FROM UserRole entity WHERE entity.user.id = :userId AND entity.role.id = :roleId")
    UserRole getUserRoleByUserIdAndRoleId(@Param("userId") UUID userId, @Param("roleId") UUID roleId);
}
