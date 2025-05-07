package com.buihien.datn.repository;

import com.buihien.datn.domain.PersonFamilyRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonFamilyRelationshipRepository extends JpaRepository<PersonFamilyRelationship, UUID> {
}
