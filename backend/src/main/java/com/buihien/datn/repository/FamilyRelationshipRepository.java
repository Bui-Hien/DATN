package com.buihien.datn.repository;

import com.buihien.datn.domain.FamilyRelationship;
import com.buihien.datn.domain.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FamilyRelationshipRepository extends JpaRepository<FamilyRelationship, UUID> {
    @Query("SELECT entity FROM FamilyRelationship entity WHERE entity.code = :code")
    FamilyRelationship findByCode(@Param("code") String code);
}
