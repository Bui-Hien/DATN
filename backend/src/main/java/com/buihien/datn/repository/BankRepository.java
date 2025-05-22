package com.buihien.datn.repository;

import com.buihien.datn.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<Bank, UUID> {
    @Query("SELECT entity FROM Bank entity WHERE entity.code = :code")
    Bank findByCode(@Param("code") String code);}
