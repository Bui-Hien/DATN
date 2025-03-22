package com.buihien.datn.repository;

import com.buihien.datn.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT entity FROM User entity WHERE entity.username= :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT entity.role.name FROM UserRole entity WHERE entity.user.id = :userId")
    List<String> findAllRolesByUserId(@Param("userId") Long userId);

    @Query("SELECT entity.role.name FROM UserRole entity WHERE entity.user.username = :username")
    List<String> findAllRolesByUserName(@Param("username") String username);

    @Query("SELECT entity.user FROM UserRole entity WHERE entity.role.name = :role")
    Optional<User> findUserByRole(@Param("role") String role);
}
