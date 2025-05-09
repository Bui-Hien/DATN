package com.buihien.datn.dto;

import com.buihien.datn.domain.SalaryResultItem;
import com.buihien.datn.domain.SalaryTemplate;

import java.util.List;

public class SalaryResultItemDto extends AuditableDto {
    private SalaryResultDto salaryResult;
    private StaffDto staff;
    private Double totalSalary;
    private List<SalaryResultItemDetailDto> salaryResultItemDetails;

    public SalaryResultItemDto() {
    }

    public SalaryResultItemDto(SalaryResultItem entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.salaryResult = new SalaryResultDto(entity.getSalaryResult(), false);
            this.staff = new StaffDto(entity.getStaff(), false);
            this.totalSalary = entity.getTotalSalary();
        }
    }

    public SalaryResultDto getSalaryResult() {
        return salaryResult;
    }

    public void setSalaryResult(SalaryResultDto salaryResult) {
        this.salaryResult = salaryResult;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public List<SalaryResultItemDetailDto> getSalaryResultItemDetails() {
        return salaryResultItemDetails;
    }

    public void setSalaryResultItemDetails(List<SalaryResultItemDetailDto> salaryResultItemDetails) {
        this.salaryResultItemDetails = salaryResultItemDetails;
    }
}
