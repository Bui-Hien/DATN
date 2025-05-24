export class SearchObject {
    constructor() {
        this.id = null;
        this.ownerId = null;
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

        this.fromRecruitmentDate = null;
        this.toRecruitmentDate = null;
        this.fromStartDate = null;
        this.toStartDate = null;
        this.employeeStatus = null;
        this.staffPhase = null;
        this.gender = null;
        this.maritalStatus = null;
        this.educationLevel = null;
        this.department = null;
    }
}
