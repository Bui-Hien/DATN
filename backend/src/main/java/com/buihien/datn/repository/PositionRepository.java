package com.buihien.datn.repository;

import com.buihien.datn.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
    @Query("SELECT p FROM Position p WHERE p.code = :code")
    Optional<Position> findByCode(@Param("code") String code);
}
