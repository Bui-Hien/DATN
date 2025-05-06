package com.buihien.datn.domain;

import jakarta.persistence.*;

//Nhân viên phải chấm công vào địa chỉ IP phòng ban quản lý mình

@Table(name = "tbl_hr_department_ip")
@Entity
public class DepartmentIp extends AuditableEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;// phong ban

    @Column(name = "ip_address")
    private String ipAddress;// dia chi ip

    @Column(name = "description")
    private String description;// mo ta

    public DepartmentIp() {
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
