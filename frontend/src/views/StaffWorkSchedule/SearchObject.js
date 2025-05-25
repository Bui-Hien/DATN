export class SearchObject {
    constructor() {
        this.id = null;
        this.ownerId = null;//nhân viên
        this.pageIndex = 1;
        this.pageSize = 10;
        this.keyword = '';
        this.fromDate = null;
        this.toDate = null;
        this.voided = null;
        this.orderBy = false;
        this.roleId = null;
        this.parentId = null;
        this.exportExcel = false;
        this.departmentId = null;
        this.shiftWorkStatus = null;
        this.timeSheetDetail = false;
    }
}
