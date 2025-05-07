package com.buihien.datn.dto;

import com.buihien.datn.domain.StaffSalaryItem;

public class StaffSalaryItemDto extends AuditableDto {
    private StaffSalaryTemplateDto staffSalaryTemplate;
    private SalaryTemplateItemDto templateItem;
    private Double value; // giá trị thực tế dùng cho nhân viên này

    public StaffSalaryItemDto() {
    }

    public StaffSalaryItemDto(StaffSalaryItem entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            if (entity.getStaffSalaryTemplate() != null) {
                this.staffSalaryTemplate = new StaffSalaryTemplateDto(entity.getStaffSalaryTemplate(), false, false);
            }
            if (entity.getTemplateItem() != null) {
                this.templateItem = new SalaryTemplateItemDto(entity.getTemplateItem(), false);
            }
            this.value = entity.getValue();
            if (isGetFull) {
                //todo
            }
        }
    }

    public StaffSalaryTemplateDto getStaffSalaryTemplate() {
        return staffSalaryTemplate;
    }

    public void setStaffSalaryTemplate(StaffSalaryTemplateDto staffSalaryTemplate) {
        this.staffSalaryTemplate = staffSalaryTemplate;
    }

    public SalaryTemplateItemDto getTemplateItem() {
        return templateItem;
    }

    public void setTemplateItem(SalaryTemplateItemDto templateItem) {
        this.templateItem = templateItem;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
