package com.buihien.datn.repository;

import com.buihien.datn.domain.PersonBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersonBankAccountRepository extends JpaRepository<PersonBankAccount, UUID> {
    @Query("SELECT entity FROM PersonBankAccount entity " +
            "WHERE entity.person.id = :personId and entity.bank.id = :bankId")
    PersonBankAccount findByPersonIdAndBankId(
            @Param("personId") UUID peronId, @Param("bankId") UUID bankId);

    @Query("SELECT entity FROM PersonBankAccount entity " +
            "WHERE entity.person.id = :personId AND entity.isMain IS TRUE")
    List<PersonBankAccount> findPersonBankAccountByIsMain(@Param("personId") UUID personId);

}
