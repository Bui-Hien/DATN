package com.buihien.datn.repository;

import com.buihien.datn.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
    @Query("SELECT c.candidateCode FROM Candidate c WHERE c.candidateCode LIKE CONCAT(:prefix, '%')")
    List<String> findCandidateCodesStartingWith(@Param("prefix") String prefix);}
