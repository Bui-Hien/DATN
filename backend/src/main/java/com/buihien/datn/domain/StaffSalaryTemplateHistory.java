package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;

@Table(
        name = "tbl_staff_salary_template",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"staff_id", "salary_template_id"})
        }
)
@Entity
public class StaffSalaryTemplateHistory extends AuditableEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id")
    private Staff staff; // nhân viên dùng mẫu bảng lương

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_template_id")
    private SalaryTemplate salaryTemplate; // Mẫu bảng lương được dùng

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = false;

    public StaffSalaryTemplateHistory() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public SalaryTemplate getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplate salaryTemplate) {
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
