package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.SalaryTemplateItem;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Valid
public class SalaryTemplateItemDto extends BaseObjectDto {
    @NotNull(message = "Thứ tự hiển thị không được để trống")
    @Min(value = 1, message = "Thứ tự hiển thị phải lớn hơn 0")
    private Integer displayOrder; // Thứ tự hiển thị
    private SalaryTemplateDto salaryTemplate; // thuộc mẫu bang luong nao
    @ValidEnumValue(enumClass = DatnConstants.SalaryItemType.class, message = "Loại thành phần lương không hợp lệ")
    private Integer salaryItemType; // DatnConstants.SalaryItemType
    private Double defaultAmount;
    private String formula; // nếu type là USING_FORMULA thì lưu công thức

    public SalaryTemplateItemDto() {
    }

    public SalaryTemplateItemDto(SalaryTemplateItem entity, Boolean isGetSalaryTemplate) {
        super(entity);
        if (entity != null) {
            this.displayOrder = entity.getDisplayOrder();
            this.defaultAmount = entity.getDefaultAmount();
            this.salaryItemType = entity.getSalaryItemType();
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

    public Integer getSalaryItemType() {
        return salaryItemType;
    }

    public void setSalaryItemType(Integer salaryItemType) {
        this.salaryItemType = salaryItemType;
    }

    public Double getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(Double defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
