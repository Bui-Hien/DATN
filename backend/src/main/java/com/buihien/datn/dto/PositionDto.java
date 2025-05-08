package com.buihien.datn.dto;

import com.buihien.datn.domain.Position;

public class PositionDto extends BaseObjectDto {
    private DepartmentDto department;
    private StaffDto staff;
    private Boolean isMain;

    public PositionDto() {
    }

    public PositionDto(Position entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            this.department = new DepartmentDto(entity.getDepartment(), false, false, false);
            this.staff = new StaffDto(entity.getStaff(), false);
            this.isMain = entity.getMain();
            if (isGetFull) {
                //todo
            }
        }
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
        this.staff = staff;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }

}
