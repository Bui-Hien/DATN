package com.buihien.datn.domain;

import jakarta.persistence.*;

import java.util.Date;

@Table(name = "tbl_staff_labour_agreement")
@Entity
public class StaffLabourAgreement extends AuditableEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    private Integer contractType;// thu viec, chinh thuc, thoi vu, xac dinh thoi han, khong xac dinh thoi han

    @Column(name = "labour_agreement_number")
    private String labourAgreementNumber;// so hop dong

    @Column(name = "start_date")
    private Date startDate; // ngay bat dau hieu luc

    @Column(name = "end_date")
    private Date endDate;// ngay ap dung cuoi cung

    @Column(name = "duration_months")
    private Integer durationMonths; // số tháng hợp đồng (chỉ áp dụng khi loại hợp đồng là xác định thời hạn)

    @Column(name = "working_hour")
    private Double workingHour;// gio cong chuan 1 ngay

    @Column(name = "working_hour_week_min")
    private Double workingHourWeekMin;// gio cong toi thieu 1 tuan

    @Column(name = "salary")
    private Double salary;// muc luong

    @Column(name = "signed_date")
    private Date signedDate;// Ngày ký

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salary_template_id")
    private SalaryTemplate salaryTemplate; // mẫu bảng lương


    //bhxh
    @Column(name = "social_insurance_number", nullable = true)
    private String socialInsuranceNumber;// Số sổ bảo hiểm xã hội

    @Column(name = "start_ins_date")
    private Date startInsDate; // Ngày bắt đầu tham gia bảo hiểm xã hội

    // Mức lương tham gia bảo hiểm xã hội
    @Column(name = "insurance_salary")
    private Double insuranceSalary;

    // Tỷ lệ đóng BHXH của nhân viên
    @Column(name = "staff_si_percentage")
    private Double staffSocialInsurancePercentage;

    // Tỷ lệ đóng BHYT của nhân viên
    @Column(name = "staff_hi_percentage")
    private Double staffHealthInsurancePercentage;

    // Tỷ lệ đóng BHTN của nhân viên
    @Column(name = "staff_ui_percentage")
    private Double staffUnemploymentInsurancePercentage;

    // Tỷ lệ đóng BHXH của công ty
    @Column(name = "org_si_percentage")
    private Double orgSocialInsurancePercentage;

    // Tỷ lệ đóng BHYT của công ty
    @Column(name = "org_hi_percentage")
    private Double orgHealthInsurancePercentage;

    // Tỷ lệ đóng BHTN của công ty
    @Column(name = "org_ui_percentage")
    private Double orgUnemploymentInsurancePercentage;

    @Column(name = "paid_status")
    private Integer paidStatus; // Bảo hiểm này của nhan vien da duoc tra (dong) hay chua. Chi tiet: DatnConstants.StaffSocialInsurancePaidStatus

    @Column(name = "insurance_start_date")
    private Date insuranceStartDate;//Ngày bắt đầu mức đóng

    @Column(name = "insurance_end_date")
    private Date insuranceEndDate;//Ngày kết thúc mức đóng

    @Column(name = "agreement_status")
    private Integer agreementStatus; // Trạng thái hợp đồng. Chi tiết DatnConstants.StaffLabourAgreementStatus;

    public StaffLabourAgreement() {
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getLabourAgreementNumber() {
        return labourAgreementNumber;
    }

    public void setLabourAgreementNumber(String labourAgreementNumber) {
        this.labourAgreementNumber = labourAgreementNumber;
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

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public Double getWorkingHour() {
        return workingHour;
    }

    public void setWorkingHour(Double workingHour) {
        this.workingHour = workingHour;
    }

    public Double getWorkingHourWeekMin() {
        return workingHourWeekMin;
    }

    public void setWorkingHourWeekMin(Double workingHourWeekMin) {
        this.workingHourWeekMin = workingHourWeekMin;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public SalaryTemplate getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplate salaryTemplate) {
        this.salaryTemplate = salaryTemplate;
    }

    public String getSocialInsuranceNumber() {
        return socialInsuranceNumber;
    }

    public void setSocialInsuranceNumber(String socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    public Date getStartInsDate() {
        return startInsDate;
    }

    public void setStartInsDate(Date startInsDate) {
        this.startInsDate = startInsDate;
    }

    public Double getInsuranceSalary() {
        return insuranceSalary;
    }

    public void setInsuranceSalary(Double insuranceSalary) {
        this.insuranceSalary = insuranceSalary;
    }

    public Double getStaffSocialInsurancePercentage() {
        return staffSocialInsurancePercentage;
    }

    public void setStaffSocialInsurancePercentage(Double staffSocialInsurancePercentage) {
        this.staffSocialInsurancePercentage = staffSocialInsurancePercentage;
    }

    public Double getStaffHealthInsurancePercentage() {
        return staffHealthInsurancePercentage;
    }

    public void setStaffHealthInsurancePercentage(Double staffHealthInsurancePercentage) {
        this.staffHealthInsurancePercentage = staffHealthInsurancePercentage;
    }

    public Double getStaffUnemploymentInsurancePercentage() {
        return staffUnemploymentInsurancePercentage;
    }

    public void setStaffUnemploymentInsurancePercentage(Double staffUnemploymentInsurancePercentage) {
        this.staffUnemploymentInsurancePercentage = staffUnemploymentInsurancePercentage;
    }

    public Double getOrgSocialInsurancePercentage() {
        return orgSocialInsurancePercentage;
    }

    public void setOrgSocialInsurancePercentage(Double orgSocialInsurancePercentage) {
        this.orgSocialInsurancePercentage = orgSocialInsurancePercentage;
    }

    public Double getOrgHealthInsurancePercentage() {
        return orgHealthInsurancePercentage;
    }

    public void setOrgHealthInsurancePercentage(Double orgHealthInsurancePercentage) {
        this.orgHealthInsurancePercentage = orgHealthInsurancePercentage;
    }

    public Double getOrgUnemploymentInsurancePercentage() {
        return orgUnemploymentInsurancePercentage;
    }

    public void setOrgUnemploymentInsurancePercentage(Double orgUnemploymentInsurancePercentage) {
        this.orgUnemploymentInsurancePercentage = orgUnemploymentInsurancePercentage;
    }

    public Integer getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(Integer paidStatus) {
        this.paidStatus = paidStatus;
    }

    public Date getInsuranceStartDate() {
        return insuranceStartDate;
    }

    public void setInsuranceStartDate(Date insuranceStartDate) {
        this.insuranceStartDate = insuranceStartDate;
    }

    public Date getInsuranceEndDate() {
        return insuranceEndDate;
    }

    public void setInsuranceEndDate(Date insuranceEndDate) {
        this.insuranceEndDate = insuranceEndDate;
    }

    public Integer getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(Integer agreementStatus) {
        this.agreementStatus = agreementStatus;
    }
}
