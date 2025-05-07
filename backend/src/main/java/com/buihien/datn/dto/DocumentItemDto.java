package com.buihien.datn.dto;

import com.buihien.datn.domain.DocumentItem;

public class DocumentItemDto extends AuditableDto {
    private DocumentTemplateDto documentTemplate; // Thuộc bộ tài liệu nào
    private Integer displayOrder; // thứ tự hiển thị
    private Boolean isRequired = false; //Cần phải nộp

    public DocumentItemDto() {
    }

    public DocumentItemDto(DocumentItem entity, Boolean isGetDocumentTemplate) {
        super(entity);
        if (entity != null) {
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }
}
