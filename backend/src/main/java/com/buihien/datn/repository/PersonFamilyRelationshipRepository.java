package com.buihien.datn.repository;

import com.buihien.datn.domain.PersonFamilyRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonFamilyRelationshipRepository extends JpaRepository<PersonFamilyRelationship, UUID> {
    @Query("SELECT entity FROM PersonFamilyRelationship entity " +
            "WHERE entity.person.id = :peronId and entity.familyRelationship.id = :familyRelationshipId")
    PersonFamilyRelationship findByPersonIdAndFamilyRelationshipId(
            @Param("peronId") UUID peronId, @Param("familyRelationshipId") UUID familyRelationshipId);
}
