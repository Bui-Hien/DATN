package com.buihien.datn.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_hr_document_item")
public class    DocumentItem extends AuditableEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_template_id")
    private DocumentTemplate documentTemplate; // Thuộc bộ tài liệu nào
    @Column(name="name")
    private String name; // tên tài liệu
    @Column(name="description")
    private String description; // mô tả tài liệu
    @Column(name = "display_order")
    private Integer displayOrder; // thứ tự hiển thị
    @Column(name = "is_required")
    private Boolean isRequired = false; //Cần phải nộp

    public DocumentItem() {
    }

    public DocumentTemplate getDocumentTemplate() {
        return documentTemplate;
    }

    public void setDocumentTemplate(DocumentTemplate documentTemplate) {
        this.documentTemplate = documentTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean required) {
        isRequired = required;
    }
}
