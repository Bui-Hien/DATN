package com.buihien.datn.repository;

import com.buihien.datn.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    @Query("SELECT entity FROM Certificate entity WHERE entity.code = :code AND entity.person.id =:personId")
    List<Certificate> findCertificateByCode(@Param("code") String code, @Param("personId") String personId);
}
