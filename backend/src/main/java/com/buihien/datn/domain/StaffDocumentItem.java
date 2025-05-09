package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tbl_staff_document_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"staff_id", "document_item"})
        })

public class StaffDocumentItem extends AuditableEntity {
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "document_item")
    private DocumentItem documentItem;

    @OneToOne
    @JoinColumn(name = "file_id")
    private FileDescription documentFile; // file đính kèm nếu có

    @Column(name = "submit_date")
    private Date submitDate; // Ngày nộp tài liệu

    public StaffDocumentItem() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public DocumentItem getDocumentItem() {
        return documentItem;
    }

    public void setDocumentItem(DocumentItem documentItem) {
        this.documentItem = documentItem;
    }

    public FileDescription getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(FileDescription documentFile) {
        this.documentFile = documentFile;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }
}
