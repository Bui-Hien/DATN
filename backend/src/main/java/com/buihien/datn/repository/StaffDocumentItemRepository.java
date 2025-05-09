package com.buihien.datn.repository;

import com.buihien.datn.domain.PersonBankAccount;
import com.buihien.datn.domain.StaffDocumentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StaffDocumentItemRepository extends JpaRepository<StaffDocumentItem, UUID> {
    @Query("SELECT entity FROM StaffDocumentItem entity WHERE entity.staff.id = :staffId and entity.documentItem.id = :documentItemId")
    StaffDocumentItem findByStaffIdAndDocumentItemId(@Param("staffId") UUID staffId, @Param("documentItemId") UUID documentItemId);
}
