package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tbl_hr_document_template")
public class DocumentTemplate extends BaseObject {
    @OneToMany(mappedBy = "documentTemplate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentItem> documentItems; // các tài liệu trong bộ hồ sơ/tài liệu

    public DocumentTemplate() {
    }

    public Set<DocumentItem> getDocumentItems() {
        return documentItems;
    }

    public void setDocumentItems(Set<DocumentItem> documentItems) {
        this.documentItems = documentItems;
    }
}
