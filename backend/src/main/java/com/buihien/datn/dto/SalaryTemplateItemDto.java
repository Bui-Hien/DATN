package com.buihien.datn.dto;

import com.buihien.datn.domain.SalaryTemplateItem;

public class SalaryTemplateItemDto extends BaseObjectDto {
    private Integer displayOrder; // Thứ tự hiển thị
    private SalaryTemplateDto salaryTemplate; // thuộc mẫu bang luong nao
    private Double value;
    private String formula; // nếu type là USING_FORMULA thì lưu công thức

    public SalaryTemplateItemDto() {
    }

    public SalaryTemplateItemDto(SalaryTemplateItem entity, Boolean isGetSalaryTemplate) {
        super(entity);
        if (entity != null) {
            this.displayOrder = entity.getDisplayOrder();
            this.value = entity.getValue();
            this.formula = entity.getFormula();

            if (isGetSalaryTemplate) {
                if (entity.getSalaryTemplate() != null) {
                    this.salaryTemplate = new SalaryTemplateDto(entity.getSalaryTemplate(), false);
                }
            }

        }
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public SalaryTemplateDto getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplateDto salaryTemplate) {
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
