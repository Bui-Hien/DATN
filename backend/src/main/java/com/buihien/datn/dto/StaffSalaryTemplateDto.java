package com.buihien.datn.dto;

import com.buihien.datn.domain.StaffSalaryItem;
import com.buihien.datn.domain.StaffSalaryTemplate;

import java.util.ArrayList;
import java.util.List;

public class StaffSalaryTemplateDto extends AuditableDto {
    private StaffDto staff; // nhân viên dùng mẫu bảng lương
    private SalaryTemplateDto salaryTemplate; // Mẫu bảng lương được dùng
    private List<StaffSalaryItemDto> staffSalaryItems;

    public StaffSalaryTemplateDto() {
    }

    public StaffSalaryTemplateDto(StaffSalaryTemplate entity, Boolean isGetSalaryTemplate, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            if (entity.getStaff() != null) {
                this.staff = new StaffDto(entity.getStaff(), false);
            }
            if (entity.getSalaryTemplate() != null && isGetSalaryTemplate) {
                this.salaryTemplate = new SalaryTemplateDto(entity.getSalaryTemplate(), false);
            }

            if (isGetFull) {
                if (entity.getStaffSalaryItems() != null && !entity.getStaffSalaryItems().isEmpty()) {
                    this.staffSalaryItems = new ArrayList<>();
                    for (StaffSalaryItem item : entity.getStaffSalaryItems()) {
                        this.staffSalaryItems.add(new StaffSalaryItemDto(item, false));
                    }
                }
            }
        }
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public SalaryTemplateDto getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplateDto salaryTemplate) {
        this.salaryTemplate = salaryTemplate;
    }

    public List<StaffSalaryItemDto> getStaffSalaryItems() {
        return staffSalaryItems;
    }

    public void setStaffSalaryItems(List<StaffSalaryItemDto> staffSalaryItems) {
        this.staffSalaryItems = staffSalaryItems;
    }
}
