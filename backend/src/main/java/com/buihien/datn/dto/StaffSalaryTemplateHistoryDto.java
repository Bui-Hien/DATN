package com.buihien.datn.dto;

import com.buihien.datn.domain.StaffSalaryTemplateHistory;

import java.util.Date;

public class StaffSalaryTemplateHistoryDto extends AuditableDto {
    private StaffDto staff; // nhân viên dùng mẫu bảng lương
    private SalaryTemplateDto salaryTemplate; // Mẫu bảng lương được dùng
    private Date startDate;
    private Date endDate;
    private Boolean isCurrent = false;

    public StaffSalaryTemplateHistoryDto() {
    }

    public StaffSalaryTemplateHistoryDto(StaffSalaryTemplateHistory entity) {
        super(entity);
        if (entity != null) {
            if (entity.getStaff() != null) {
                this.staff = new StaffDto(entity.getStaff(), false);
            }
            if (entity.getSalaryTemplate() != null) {
                this.salaryTemplate = new SalaryTemplateDto(entity.getSalaryTemplate(), false);
            }
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.isCurrent = entity.getIsCurrent();
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

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean current) {
        isCurrent = current;
    }
}
