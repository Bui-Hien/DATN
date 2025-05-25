export class StaffWorkScheduleObject {
    constructor() {
        this.id = null;
        this.shiftWorkType = null;
        this.staff = null;
        this.workingDate = null;
        this.checkIn = new Date();
        this.checkOut = null;
        this.shiftWorkStatus = null;
        this.coordinator = null;
        this.isLocked = null;
    }
}
