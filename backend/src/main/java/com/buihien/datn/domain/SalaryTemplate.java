package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Set;

@Table(name = "tbl_salary_template")
@Entity
public class SalaryTemplate extends BaseObject{
    @OneToMany(mappedBy = "salaryTemplate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder")
    private Set<SalaryTemplateItem> templateItems; // thành phần lương chính là các cột trong mẫu bảng lương

    public SalaryTemplate() {
    }

    public Set<SalaryTemplateItem> getTemplateItems() {
        return templateItems;
    }

    public void setTemplateItems(Set<SalaryTemplateItem> templateItems) {
        this.templateItems = templateItems;
    }
}
