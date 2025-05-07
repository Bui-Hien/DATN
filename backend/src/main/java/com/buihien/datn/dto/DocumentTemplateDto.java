package com.buihien.datn.dto;

import com.buihien.datn.domain.DocumentItem;
import com.buihien.datn.domain.DocumentTemplate;
import com.buihien.datn.domain.StaffDocumentItem;

import java.util.ArrayList;
import java.util.List;

public class DocumentTemplateDto extends BaseObjectDto {
    private List<DocumentItemDto> documentItems; // các tài liệu trong bộ hồ sơ/tài liệu
    private List<StaffDocumentItemDto> staffDocumentItems;

    public DocumentTemplateDto() {
    }

    public DocumentTemplateDto(DocumentTemplate entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            if (isGetFull) {
                if (entity.getDocumentItems() != null && !entity.getDocumentItems().isEmpty()) {
                    this.documentItems = new ArrayList<>();
                    for (DocumentItem item : entity.getDocumentItems()) {
                        this.documentItems.add(new DocumentItemDto(item, false));
                    }
                }

                if (entity.getStaffDocumentItems() != null && !entity.getStaffDocumentItems().isEmpty()) {
                    this.staffDocumentItems = new ArrayList<>();
                    for (StaffDocumentItem item : entity.getStaffDocumentItems()) {
                        this.staffDocumentItems.add(new StaffDocumentItemDto(item, false, false));
                    }
                }
            }
        }
    }

    public List<DocumentItemDto> getDocumentItems() {
        return documentItems;
    }

    public void setDocumentItems(List<DocumentItemDto> documentItems) {
        this.documentItems = documentItems;
    }

    public List<StaffDocumentItemDto> getStaffDocumentItems() {
        return staffDocumentItems;
    }

    public void setStaffDocumentItems(List<StaffDocumentItemDto> staffDocumentItems) {
        this.staffDocumentItems = staffDocumentItems;
    }
}
