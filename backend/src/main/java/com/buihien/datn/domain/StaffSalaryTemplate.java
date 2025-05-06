package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Set;

@Table(
        name = "tbl_staff_salary_template",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"staff_id", "salary_template_id"})
        }
)
@Entity
public class StaffSalaryTemplate extends AuditableEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id")
    private Staff staff; // nhân viên dùng mẫu bảng lương

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_template_id")
    private SalaryTemplate salaryTemplate; // Mẫu bảng lương được dùng

    @OneToMany(mappedBy = "staffSalaryTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StaffSalaryItem> staffSalaryItems;

    public StaffSalaryTemplate() {
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

    public Set<StaffSalaryItem> getStaffSalaryItems() {
        return staffSalaryItems;
    }

    public void setStaffSalaryItems(Set<StaffSalaryItem> staffSalaryItems) {
        this.staffSalaryItems = staffSalaryItems;
    }
}
