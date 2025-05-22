package com.buihien.datn.dto;

import com.buihien.datn.domain.StaffDocumentItem;

import java.util.Date;

public class StaffDocumentItemDto extends AuditableDto {
    private StaffDto staff;
    private DocumentItemDto documentItem;
    private FileDescriptionDto documentFile; // file tài liệu của nhân viên này
    private Date submitDate; // Ngày nộp tài liệu


    public StaffDocumentItemDto() {
    }

    public StaffDocumentItemDto(StaffDocumentItem entity, Boolean isGetStaff) {
        super(entity);
        if (entity != null) {
            if (isGetStaff && entity.getStaff() != null) {
                this.staff = new StaffDto();
                this.staff.setId(entity.getStaff().getId());
                this.staff.setStaffCode(entity.getStaff().getStaffCode());
                this.staff.setDisplayName(entity.getStaff().getDisplayName());
            }
            if (entity.getDocumentFile() != null) {
                this.documentFile = new FileDescriptionDto(entity.getDocumentFile());
            }

            if (entity.getDocumentItem() != null) {
                this.documentItem = new DocumentItemDto(entity.getDocumentItem(), false);
            }
            this.submitDate = entity.getSubmitDate();
        }
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
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

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }
}
