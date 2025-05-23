package com.buihien.datn.dto.CalculatedWorkingDay;

public class ShiftWorkTypeCount {
    Integer type;
    Long count;

    public ShiftWorkTypeCount() {
    }

    public ShiftWorkTypeCount(Integer type, Long count) {
        this.type = type;
        this.count = count;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
