package com.buihien.datn.dto;

import com.buihien.datn.domain.SalaryTemplate;
import com.buihien.datn.domain.SalaryTemplateItem;
import com.buihien.datn.domain.StaffSalaryTemplate;

import java.util.ArrayList;
import java.util.List;

public class SalaryTemplateDto extends BaseObjectDto {
    private List<SalaryTemplateItemDto> templateItems; // thành phần lương chính là các cột trong mẫu bảng lương
    private List<StaffSalaryTemplateDto> staffSalaryTemplates; // các nhân viên sử dụng mẫu bảng lương

    public SalaryTemplateDto() {
    }

    public SalaryTemplateDto(SalaryTemplate entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            if (isGetFull) {
                if (entity.getTemplateItems() != null && !entity.getTemplateItems().isEmpty()) {
                    this.templateItems = new ArrayList<>();
                    for (SalaryTemplateItem item : entity.getTemplateItems()) {
                        this.templateItems.add(new SalaryTemplateItemDto(item, false));
                    }
                }

                if (entity.getStaffSalaryTemplates() != null && !entity.getStaffSalaryTemplates().isEmpty()) {
                    this.staffSalaryTemplates = new ArrayList<>();
                    for (StaffSalaryTemplate item : entity.getStaffSalaryTemplates()) {
                        this.staffSalaryTemplates.add(new StaffSalaryTemplateDto(item, false, false));
                    }
                }
            }
        }
    }

    public List<SalaryTemplateItemDto> getTemplateItems() {
        return templateItems;
    }

    public void setTemplateItems(List<SalaryTemplateItemDto> templateItems) {
        this.templateItems = templateItems;
    }

    public List<StaffSalaryTemplateDto> getStaffSalaryTemplates() {
        return staffSalaryTemplates;
    }

    public void setStaffSalaryTemplates(List<StaffSalaryTemplateDto> staffSalaryTemplates) {
        this.staffSalaryTemplates = staffSalaryTemplates;
    }
}
