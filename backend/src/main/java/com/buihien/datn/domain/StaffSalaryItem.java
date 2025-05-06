package com.buihien.datn.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_staff_salary_item")
public class StaffSalaryItem extends AuditableEntity{
    @ManyToOne
    @JoinColumn(name = "staff_salary_template_id")
    private StaffSalaryTemplate staffSalaryTemplate;

    @ManyToOne
    @JoinColumn(name = "template_item_id")
    private SalaryTemplateItem templateItem;

    @Column(name = "value")
    private Double value; // giá trị thực tế dùng cho nhân viên này

    public StaffSalaryItem() {
    }

    public StaffSalaryTemplate getStaffSalaryTemplate() {
        return staffSalaryTemplate;
    }

    public void setStaffSalaryTemplate(StaffSalaryTemplate staffSalaryTemplate) {
        this.staffSalaryTemplate = staffSalaryTemplate;
    }

    public SalaryTemplateItem getTemplateItem() {
        return templateItem;
    }

    public void setTemplateItem(SalaryTemplateItem templateItem) {
        this.templateItem = templateItem;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
