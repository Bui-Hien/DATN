package com.buihien.datn.dto;

import com.buihien.datn.domain.DocumentItem;
import com.buihien.datn.domain.DocumentTemplate;

import java.util.ArrayList;
import java.util.List;

public class DocumentTemplateDto extends BaseObjectDto {
    private List<DocumentItemDto> documentItems; // các tài liệu trong bộ hồ sơ/tài liệu

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
            }
        }
    }

    public List<DocumentItemDto> getDocumentItems() {
        return documentItems;
    }

    public void setDocumentItems(List<DocumentItemDto> documentItems) {
        this.documentItems = documentItems;
    }
}
