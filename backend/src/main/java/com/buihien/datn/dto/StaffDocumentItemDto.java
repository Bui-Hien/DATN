package com.buihien.datn.dto;

import com.buihien.datn.domain.StaffDocumentItem;

public class StaffDocumentItemDto extends AuditableDto {
    private DocumentTemplateDto documentTemplate;
    private DocumentItemDto documentItem;
    private FileDescriptionDto documentFile; // file tài liệu của nhân viên này

    public StaffDocumentItemDto() {
    }

    public StaffDocumentItemDto(StaffDocumentItem entity, Boolean isGetDocumentTemplate, Boolean isGetDocumentItem) {
        super(entity);
        if (entity != null) {
            if (entity.getDocumentTemplate() != null) {
                this.documentFile = new FileDescriptionDto(entity.getDocumentFile());
            }
            if (entity.getDocumentTemplate() != null && isGetDocumentTemplate) {
                this.documentTemplate = new DocumentTemplateDto(entity.getDocumentTemplate(), false);
            }

            if (entity.getDocumentItem() != null && isGetDocumentItem) {
                this.documentItem = new DocumentItemDto(entity.getDocumentItem(), false);
            }
        }
    }

    public DocumentTemplateDto getDocumentTemplate() {
        return documentTemplate;
    }

    public void setDocumentTemplate(DocumentTemplateDto documentTemplate) {
        this.documentTemplate = documentTemplate;
    }

    public DocumentItemDto getDocumentItem() {
        return documentItem;
    }

    public void setDocumentItem(DocumentItemDto documentItem) {
        this.documentItem = documentItem;
    }

    public FileDescriptionDto getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(FileDescriptionDto documentFile) {
        this.documentFile = documentFile;
    }
}
