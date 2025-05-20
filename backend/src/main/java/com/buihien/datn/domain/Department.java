package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tbl_department")
public class Department extends BaseObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Department parent; // Phòng ban cha

    @OneToMany(mappedBy = "parent")
    private Set<Department> subDepartments; // Danh sách phòng ban con

    @ManyToOne
    @JoinColumn(name = "staff_manager_id")
    private Staff staffManager; // Vị trí quản lý

    public Department() {
    }

    public Department getParent() {
        return parent;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    public Set<Department> getSubDepartments() {
        return subDepartments;
    }

    public void setSubDepartments(Set<Department> subDepartments) {
        this.subDepartments = subDepartments;
    }

    public Staff getStaffManager() {
        return staffManager;
    }

    public void setStaffManager(Staff staffManager) {
        this.staffManager = staffManager;
    }
}
