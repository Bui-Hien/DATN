package com.buihien.datn.dto;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.StaffLabourAgreement;
import com.buihien.datn.dto.validator.ValidEnumValue;
import jakarta.validation.Valid;

import java.util.Date;

@Valid
public class StaffLabourAgreementDto extends AuditableDto {
    private StaffDto staff;
    @ValidEnumValue(enumClass = DatnConstants.ContractType.class, message = "Loại hợp đồng lao động không hợp lệ")
    private Integer contractType; // 1: Thử việc, 2: Chính thức, 3: Thời vụ, 4: Xác định thời hạn, 5: Không xác định thời hạn
    private String labourAgreementNumber; // Số hợp đồng
    private Date startDate; // Ngày bắt đầu hiệu lực hợp đồng
    private Date endDate; // Ngày kết thúc hợp đồng (nếu có)
    private Integer durationMonths; // Số tháng hợp đồng (chỉ áp dụng với hợp đồng xác định thời hạn)
    private Double workingHour; // Giờ làm việc chuẩn mỗi ngày
    private Double workingHourWeekMin; // Số giờ làm việc tối thiểu mỗi tuần
    private Double salary; // Mức lương chính theo hợp đồng
    private Date signedDate; // Ngày ký hợp đồng
    private SalaryTemplateDto salaryTemplate; // Mẫu bảng lương áp dụng

    // --- Bảo hiểm xã hội ---

    private String socialInsuranceNumber; // Số sổ bảo hiểm xã hội
    private Boolean hasSocialIns; // Có tham gia BHXH hay không
    private Date startInsDate; // Ngày bắt đầu tham gia bảo hiểm xã hội
    private Double insuranceSalary; // Mức lương làm căn cứ đóng bảo hiểm

    // --- Tỷ lệ nhân viên đóng ---
    private Double staffSocialInsurancePercentage; // Tỷ lệ đóng BHXH của nhân viên (%)
    private Double staffHealthInsurancePercentage; // Tỷ lệ đóng BHYT của nhân viên (%)
    private Double staffUnemploymentInsurancePercentage; // Tỷ lệ đóng BHTN của nhân viên (%)

    // --- Số tiền nhân viên đóng ---
    private Double staffSocialInsuranceAmount; // Tiền BHXH do nhân viên đóng
    private Double staffHealthInsuranceAmount; // Tiền BHYT do nhân viên đóng
    private Double staffUnemploymentInsuranceAmount; // Tiền BHTN do nhân viên đóng

    private Double staffTotalInsuranceAmount; // Tổng số tiền bảo hiểm nhân viên đóng

    // --- Tỷ lệ công ty đóng ---
    private Double orgSocialInsurancePercentage; // Tỷ lệ đóng BHXH của công ty (%)
    private Double orgHealthInsurancePercentage; // Tỷ lệ đóng BHYT của công ty (%)
    private Double orgUnemploymentInsurancePercentage; // Tỷ lệ đóng BHTN của công ty (%)

    // --- Số tiền công ty đóng ---
    private Double orgSocialInsuranceAmount; // Tiền BHXH do công ty đóng
    private Double orgHealthInsuranceAmount; // Tiền BHYT do công ty đóng
    private Double orgUnemploymentInsuranceAmount; // Tiền BHTN do công ty đóng

    private Double orgTotalInsuranceAmount; // Tổng số tiền bảo hiểm công ty đóng

    // --- Tổng tiền bảo hiểm (cả nhân viên và công ty) ---
    private Double totalInsuranceAmount;
    private Integer paidStatus; // Trạng thái thanh toán bảo hiểm. Xem DatnConstants.StaffSocialInsurancePaidStatus
    private Date insuranceStartDate; // Ngày bắt đầu áp dụng mức đóng
    private Date insuranceEndDate; // Ngày kết thúc áp dụng mức đóng

    @ValidEnumValue(enumClass = DatnConstants.StaffLabourAgreementStatus.class, message = "Trạng thái hợp đồng không hợp lệ")
    private Integer agreementStatus; // Trạng thái hợp đồng. Xem DatnConstants.StaffLabourAgreementStatus

    public StaffLabourAgreementDto(StaffLabourAgreement entity, Boolean isGetFull) {
        super(entity);
        if (entity != null) {
            if (entity.getStaff() != null) {
                this.staff = new StaffDto(entity.getStaff(), false);
            }
            this.contractType = entity.getContractType();
            this.labourAgreementNumber = entity.getLabourAgreementNumber();
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.durationMonths = entity.getDurationMonths();
            this.workingHour = entity.getWorkingHour();
            this.workingHourWeekMin = entity.getWorkingHourWeekMin();
            this.salary = entity.getSalary();
            this.signedDate = entity.getSignedDate();
            if (entity.getSalaryTemplate() != null) {
                this.salaryTemplate = new SalaryTemplateDto(entity.getSalaryTemplate(), false);
            }

            // Bảo hiểm
            this.socialInsuranceNumber = entity.getSocialInsuranceNumber();
            this.hasSocialIns = entity.getHasSocialIns();
            this.startInsDate = entity.getStartInsDate();
            this.insuranceSalary = entity.getInsuranceSalary();

            this.staffSocialInsurancePercentage = entity.getStaffSocialInsurancePercentage();
            this.staffHealthInsurancePercentage = entity.getStaffHealthInsurancePercentage();
            this.staffUnemploymentInsurancePercentage = entity.getStaffUnemploymentInsurancePercentage();

            this.orgSocialInsurancePercentage = entity.getOrgSocialInsurancePercentage();
            this.orgHealthInsurancePercentage = entity.getOrgHealthInsurancePercentage();
            this.orgUnemploymentInsurancePercentage = entity.getOrgUnemploymentInsurancePercentage();

            // Tính toán số tiền đóng bảo hiểm
            if (this.insuranceSalary != null) {
                // Nhân viên đóng
                this.staffSocialInsuranceAmount = calcAmount(this.insuranceSalary, this.staffSocialInsurancePercentage);
                this.staffHealthInsuranceAmount = calcAmount(this.insuranceSalary, this.staffHealthInsurancePercentage);
                this.staffUnemploymentInsuranceAmount = calcAmount(this.insuranceSalary, this.staffUnemploymentInsurancePercentage);
                this.staffTotalInsuranceAmount = sum(this.staffSocialInsuranceAmount, this.staffHealthInsuranceAmount, this.staffUnemploymentInsuranceAmount);

                // Công ty đóng
                this.orgSocialInsuranceAmount = calcAmount(this.insuranceSalary, this.orgSocialInsurancePercentage);
                this.orgHealthInsuranceAmount = calcAmount(this.insuranceSalary, this.orgHealthInsurancePercentage);
                this.orgUnemploymentInsuranceAmount = calcAmount(this.insuranceSalary, this.orgUnemploymentInsurancePercentage);
                this.orgTotalInsuranceAmount = sum(this.orgSocialInsuranceAmount, this.orgHealthInsuranceAmount, this.orgUnemploymentInsuranceAmount);

                // Tổng cộng
                this.totalInsuranceAmount = sum(this.staffTotalInsuranceAmount, this.orgTotalInsuranceAmount);
            }

            this.paidStatus = entity.getPaidStatus();
            this.insuranceStartDate = entity.getInsuranceStartDate();
            this.insuranceEndDate = entity.getInsuranceEndDate();
            this.agreementStatus = entity.getAgreementStatus();

            if (Boolean.TRUE.equals(isGetFull)) {
                // TODO: Bổ sung lấy thêm các thông tin liên quan nếu cần
            }
        }
    }

    // Hàm tính tiền = lương * tỷ lệ / 100
    private Double calcAmount(Double base, Double percent) {
        if (base == null || percent == null) return 0.0;
        return (base * percent) / 100.0;
    }

    // Hàm tính tổng
    private Double sum(Double... values) {
        double total = 0.0;
        for (Double val : values) {
            if (val != null) total += val;
        }
        return total;
    }

    public StaffDto getStaff() {
        return staff;
    }

    public void setStaff(StaffDto staff) {
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

    public SalaryTemplateDto getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplateDto salaryTemplate) {
        this.salaryTemplate = salaryTemplate;
    }

    public String getSocialInsuranceNumber() {
        return socialInsuranceNumber;
    }

    public void setSocialInsuranceNumber(String socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    public Boolean getHasSocialIns() {
        return hasSocialIns;
    }

    public void setHasSocialIns(Boolean hasSocialIns) {
        this.hasSocialIns = hasSocialIns;
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

    public Double getStaffSocialInsuranceAmount() {
        return staffSocialInsuranceAmount;
    }

    public void setStaffSocialInsuranceAmount(Double staffSocialInsuranceAmount) {
        this.staffSocialInsuranceAmount = staffSocialInsuranceAmount;
    }

    public Double getStaffHealthInsuranceAmount() {
        return staffHealthInsuranceAmount;
    }

    public void setStaffHealthInsuranceAmount(Double staffHealthInsuranceAmount) {
        this.staffHealthInsuranceAmount = staffHealthInsuranceAmount;
    }

    public Double getStaffUnemploymentInsuranceAmount() {
        return staffUnemploymentInsuranceAmount;
    }

    public void setStaffUnemploymentInsuranceAmount(Double staffUnemploymentInsuranceAmount) {
        this.staffUnemploymentInsuranceAmount = staffUnemploymentInsuranceAmount;
    }

    public Double getStaffTotalInsuranceAmount() {
        return staffTotalInsuranceAmount;
    }

    public void setStaffTotalInsuranceAmount(Double staffTotalInsuranceAmount) {
        this.staffTotalInsuranceAmount = staffTotalInsuranceAmount;
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

    public Double getOrgSocialInsuranceAmount() {
        return orgSocialInsuranceAmount;
    }

    public void setOrgSocialInsuranceAmount(Double orgSocialInsuranceAmount) {
        this.orgSocialInsuranceAmount = orgSocialInsuranceAmount;
    }

    public Double getOrgHealthInsuranceAmount() {
        return orgHealthInsuranceAmount;
    }

    public void setOrgHealthInsuranceAmount(Double orgHealthInsuranceAmount) {
        this.orgHealthInsuranceAmount = orgHealthInsuranceAmount;
    }

    public Double getOrgUnemploymentInsuranceAmount() {
        return orgUnemploymentInsuranceAmount;
    }

    public void setOrgUnemploymentInsuranceAmount(Double orgUnemploymentInsuranceAmount) {
        this.orgUnemploymentInsuranceAmount = orgUnemploymentInsuranceAmount;
    }

    public Double getOrgTotalInsuranceAmount() {
        return orgTotalInsuranceAmount;
    }

    public void setOrgTotalInsuranceAmount(Double orgTotalInsuranceAmount) {
        this.orgTotalInsuranceAmount = orgTotalInsuranceAmount;
    }

    public Double getTotalInsuranceAmount() {
        return totalInsuranceAmount;
    }

    public void setTotalInsuranceAmount(Double totalInsuranceAmount) {
        this.totalInsuranceAmount = totalInsuranceAmount;
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
