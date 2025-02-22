package com.buihien.datn.repository;

import com.buihien.datn.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("SELECT entity FROM UserRole entity WHERE entity.user.id = :userId AND entity.role.id = :roleId")
    UserRole getUserRoleByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
