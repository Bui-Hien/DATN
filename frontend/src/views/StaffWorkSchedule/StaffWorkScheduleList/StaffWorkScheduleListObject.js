import {getFirstDateOfMonth, getLastDateOfMonth} from "../../../LocalFunction";
import {ShiftWorkType, Weekdays} from "../../../LocalConstants";

export class StaffWorkScheduleListObject {
    constructor() {
        this.shiftWorkTypeList = [ShiftWorkType.FULL_DAY.value];
        this.staffs = [];
        this.fromWorkingDate = getFirstDateOfMonth();
        this.toWorkingDate = getLastDateOfMonth();
        // Danh sách các thứ trong tuần áp dụng (1 = Monday, ..., 7 = Sunday)
        this.weekdays = [
            Weekdays.MONDAY.value,
            Weekdays.TUESDAY.value,
            Weekdays.WEDNESDAY.value,
            Weekdays.THURSDAY.value,
            Weekdays.FRIDAY.value,
            Weekdays.SATURDAY.value
        ];
    }
}
