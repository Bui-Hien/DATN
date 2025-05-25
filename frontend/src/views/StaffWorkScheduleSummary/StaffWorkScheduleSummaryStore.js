import {makeAutoObservable} from "mobx";

import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "../StaffWorkSchedule/SearchObject";
import {getScheduleSummary} from "../StaffWorkSchedule/StaffWorkScheduleService";

export default class StaffWorkScheduleSummaryStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    isOpenFilter = false;

    constructor() {
        makeAutoObservable(this);
    }

    resetStore = () => {
        this.searchObject = JSON.parse(JSON.stringify(new SearchObject()));
        this.totalElements = 0;
        this.totalPages = 0;
        this.dataList = [];
        this.isOpenFilter = false;
    };

    getScheduleSummary = async () => {
        try {
            const newSearchObject = {
                ...this.searchObject,
                ownerId: this.searchObject.owner?.id,
                owner: null,
                departmentId: this.searchObject.department?.id,
                department: null
            }
            const response = await getScheduleSummary(newSearchObject);
            const result = response.data;
            this.dataList = result.data.content || [];
            this.totalElements = result.data.totalElements || 0;
            this.totalPages = result.data.totalPages || 0;
            console.log(this.dataList)
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    setPageIndex = async (page) => {
        this.searchObject.pageIndex = page;
        await this.getScheduleSummary();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.getScheduleSummary();
    };

    handleSetSearchObject = (searchObject) => {
        this.searchObject = {...searchObject};
    };

    handleCloseFilter = () => {
        this.isOpenFilter = false;
    };

    handleTogglePopupFilter = () => {
        this.isOpenFilter = !this.isOpenFilter;
    };
}
