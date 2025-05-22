import {StaffLabourAgreementStatus} from "../../LocalConstants";

export class StaffLabourAgreementObject {
    id = null;
    staff = null;
    labourAgreementNumber = "";
    contractType = null;
    startDate = null;
    durationMonths = 12;
    workingHour = 8.0;
    workingHourWeekMin = 30.0;
    salary = 7000000;
    signedDate = null;
    agreementStatus = StaffLabourAgreementStatus.UNSIGNED.value;

    constructor() {
    }
}
