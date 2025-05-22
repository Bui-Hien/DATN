package com.buihien.datn.dto;

import com.buihien.datn.domain.DocumentItem;

public class DocumentItemDto extends AuditableDto {
    private DocumentTemplateDto documentTemplate; // Thuộc bộ tài liệu nào
    private String name; // tên tài liệu
    private String description; // mô tả tài liệu
        private Integer displayOrder; // thứ tự hiển thị
    private Boolean isRequired = false; //Cần phải nộp

    public DocumentItemDto() {
    }

    public DocumentItemDto(DocumentItem entity, Boolean isGetDocumentTemplate) {
        super(entity);
        if (entity != null) {
            this.name = entity.getName();
            this.description = entity.getDescription();
            this.displayOrder = entity.getDisplayOrder();
            this.isRequired = entity.getIsRequired();
            if (entity.getDocumentTemplate() != null && isGetDocumentTemplate) {
                this.documentTemplate = new DocumentTemplateDto(entity.getDocumentTemplate(), false);
            }
        }
    }

    public DocumentTemplateDto getDocumentTemplate() {
        return documentTemplate;
    }

    public void setDocumentTemplate(DocumentTemplateDto documentTemplate) {
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

