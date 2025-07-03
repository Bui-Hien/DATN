import {ContractType, StaffLabourAgreementStatus} from "../../LocalConstants";

export class StaffLabourAgreementObject {
    id = null;
    staff = null;
    labourAgreementNumber = "HD00";
    contractType = ContractType.OFFICIAL.value;
    startDate = new Date();
    durationMonths = 12;
    workingHour = 8.0;
    workingHourWeekMin = 30.0;
    salary = 7000000;
    signedDate = null;
    agreementStatus = StaffLabourAgreementStatus.UNSIGNED.value;

    constructor() {
        const signedDateCalc = new Date();
        signedDateCalc.setMonth(signedDateCalc.getMonth() + 12);
        this.signedDate = signedDateCalc;
    }
}
