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

    @Column(name = "has_social_ins")
    private Boolean hasSocialIns; // Có đóng BHXH hay không

    @Column(name = "start_ins_date")
    private Date startInsDate;

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

    // Tổng tiền bảo hiểm mà nhân viên đóng
    @Column(name = "staff_total_insurance")
    private Double staffTotalInsuranceAmount;

    // Tỷ lệ đóng BHXH của công ty
    @Column(name = "org_si_percentage")
    private Double orgSocialInsurancePercentage;

    // Tỷ lệ đóng BHYT của công ty
    @Column(name = "org_hi_percentage")
    private Double orgHealthInsurancePercentage;

    // Tỷ lệ đóng BHTN của công ty
    @Column(name = "org_ui_percentage")
    private Double orgUnemploymentInsurancePercentage;

    // Tổng tiền bảo hiểm mà công ty đóng
    @Column(name = "org_total_insurance")
    private Double orgTotalInsuranceAmount;

    @Column(name = "paid_status")
    private Integer paidStatus; // Bảo hiểm này của nhan vien da duoc tra (dong) hay chua. Chi tiet: DatnConstants.StaffSocialInsurancePaidStatus

    @Column(name = "total_insurance_amount")
    private Double totalInsuranceAmount;

    @Column(name = "insurance_start_date")
    private Date insuranceStartDate;//Ngày bắt đầu mức đóng

    @Column(name = "insurance_end_date")
    private Date insuranceEndDate;//Ngày kết thúc mức đóng

    @Column(name = "agreement_status")
    private Integer agreementStatus; // Trạng thái hợp đồng. Chi tiết DatnConstants.StaffLabourAgreementStatus;
}
