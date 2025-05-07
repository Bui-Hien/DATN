package com.buihien.datn.domain;

import jakarta.persistence.*;

@Table(name = "tbl_salary_template_item")
@Entity
public class SalaryTemplateItem extends BaseObject {
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder; // Thứ tự hiển thị

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_template_id")
    private SalaryTemplate salaryTemplate; // thuộc mẫu bang luong nao

    @Column(name = "value")
    private Double value;

    @Column(name = "formula", columnDefinition = "TEXT")
    private String formula; // nếu type là USING_FORMULA thì lưu công thức

    public SalaryTemplateItem() {
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public SalaryTemplate getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplate salaryTemplate) {
        this.salaryTemplate = salaryTemplate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
