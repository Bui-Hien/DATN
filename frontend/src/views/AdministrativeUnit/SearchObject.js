export class SearchObject {
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
    level = null;
    province = null;
    district = null;
    ward = null;
    provinceId = null;
    districtId = null;
    wardId = null;

    constructor() {
        this.pageIndex = 0;
        this.pageSize = 10;
        this.keyword = '';
    }
}
