package com.buihien.datn.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_staff_document_item")
public class StaffDocumentItem extends AuditableEntity {
    @ManyToOne
    @JoinColumn(name = "document_template_id")
    private DocumentTemplate documentTemplate;

    @ManyToOne
    @JoinColumn(name = "document_item")
    private DocumentItem documentItem;

    @OneToOne
    private FileDescription documentFile; // file tài liệu của nhân viên này

    public StaffDocumentItem() {
    }

    public DocumentTemplate getDocumentTemplate() {
        return documentTemplate;
    }

    public void setDocumentTemplate(DocumentTemplate documentTemplate) {
        this.documentTemplate = documentTemplate;
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
}
