export class DocumentTemplateObject {
    id = null;
    ownerId = null;
    pageIndex = 0;
    pageSize = 10;
    keyword = '';
    fromDate = null;
    toDate = null;
    voided = null;
    orderBy = false;
    roleId = null;
    parentId = null;
    exportExcel = false;

    constructor() {
        this.id = null;
        this.ownerId = null;
        this.pageIndex = 0;
        this.pageSize = 10;
        this.keyword = '';
        this.fromDate = null;
        this.toDate = null;
        this.voided = null;
        this.orderBy = false;
        this.roleId = null;
        this.parentId = null;
        this.exportExcel = false;
    }
}
