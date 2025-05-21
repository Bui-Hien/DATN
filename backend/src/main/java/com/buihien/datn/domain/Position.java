package com.buihien.datn.domain;

import jakarta.persistence.*;

//Vị trí công tác của nhân viên,
// 1 nhân viên có thể có nhiều vị trí làm việc khác nhau
@Table(name = "tbl_position")
@Entity
public class Position extends BaseObject {

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @Column(name = "is_main")
    private Boolean isMain;

    public Position() {
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Boolean getIsMain() {
        return isMain;
    }

    public void setIsMain(Boolean main) {
        isMain = main;
    }

}
