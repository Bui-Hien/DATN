package com.buihien.datn.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

//Kỳ lương (VD: tháng 5/2025, tháng 6/2025...)
@Entity
@Table(name = "tbl_salary_period")
public class SalaryPeriod extends AuditableEntity {
    @Column(name = "name")
    private String name; // Ví dụ: "Tháng 5/2025"

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private Integer salaryPeriodStatus;//Xem status: DatnConstants.SalaryPeriodStatus

    public SalaryPeriod() {
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
