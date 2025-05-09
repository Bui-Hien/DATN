package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.SalaryPeriod;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.Date;

@Valid
public class SalaryPeriodDto extends AuditableDto {
    private String name; // Ví dụ: "Tháng 5/2025"
    private Date startDate;
    private Date endDate;
    @ValidEnumValue(enumClass = DatnConstants.SalaryPeriodStatus.class, message = "Trạng thái kỳ lương không hợp lệ")
    private Integer salaryPeriodStatus;//Xem status: DatnConstants.SalaryPeriodStatus

    public SalaryPeriodDto() {
    }

    public SalaryPeriodDto(SalaryPeriod entity) {
        super(entity);
        if(entity!= null) {
            this.name = entity.getName();
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.salaryPeriodStatus = entity.getSalaryPeriodStatus();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getSalaryPeriodStatus() {
        return salaryPeriodStatus;
    }

    public void setSalaryPeriodStatus(Integer salaryPeriodStatus) {
        this.salaryPeriodStatus = salaryPeriodStatus;
    }
}
