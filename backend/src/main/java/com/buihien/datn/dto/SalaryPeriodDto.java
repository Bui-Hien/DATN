package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.SalaryPeriod;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.Date;

@Valid
public class SalaryPeriodDto extends BaseObjectDto {
    private Date startDate;
    private Date endDate;
    @ValidEnumValue(enumClass = DatnConstants.SalaryPeriodStatus.class, message = "Trạng thái kỳ lương không hợp lệ")
    private Integer salaryPeriodStatus;//Xem status: DatnConstants.SalaryPeriodStatus
    private Integer estimatedWorkingDays; // Số ngày làm việc ước tính

    public SalaryPeriodDto() {
    }

    public SalaryPeriodDto(SalaryPeriod entity) {
        super(entity);
        if (entity != null) {
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.salaryPeriodStatus = entity.getSalaryPeriodStatus();
            this.estimatedWorkingDays = entity.getEstimatedWorkingDays();
        }
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

    public Integer getEstimatedWorkingDays() {
        return estimatedWorkingDays;
    }

    public void setEstimatedWorkingDays(Integer estimatedWorkingDays) {
        this.estimatedWorkingDays = estimatedWorkingDays;
    }
}
