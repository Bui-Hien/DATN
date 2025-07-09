import {makeAutoObservable} from "mobx";
import {
    deleteMultipleSalaryResultByIds,
    deleteSalaryResult,
    getRecalculateSalary,
    getSalaryResultById,
    pagingSalaryResult,
    saveSalaryResult,
} from "./SalaryResultService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {SalaryResultObject} from "./SalaryResult";

export default class SalaryResultStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new SalaryResultObject();
    selectedDataList = [];
    isOpenFilter = false;

    constructor() {
        makeAutoObservable(this);
    }

    resetStore = () => {
        this.searchObject = JSON.parse(JSON.stringify(new SearchObject()));
        this.totalElements = 0;
        this.totalPages = 0;
        this.dataList = [];
        this.openCreateEditPopup = false;
        this.selectedRow = null;
        this.openConfirmDeletePopup = false;
        this.openConfirmDeleteListPopup = false;
        this.selectedDataList = [];
        this.isOpenFilter = false;
    };

    pagingSalaryResult = async () => {
        try {
            const response = await pagingSalaryResult(this.searchObject);
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
        await this.pagingSalaryResult();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingSalaryResult();
    };
    getRecalculateSalary = async (id) => {
        try {
            await getRecalculateSalary(id);
            toast.success(i18n.t("toast.update_success"));
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleSetSelectedRow = async (id) => {
        try {
            if (id) {
                const {data} = await getSalaryResultById(id);
                this.selectedRow = {
                    ...new SalaryResultObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new SalaryResultObject();
            }
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    }
    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getSalaryResultById(id);
                this.selectedRow = {
                    ...new SalaryResultObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new SalaryResultObject();
            }
            this.openCreateEditPopup = true;
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleGetSalaryResultById = async (id) => {
        try {
            if (id) {
                const {data} = await getSalaryResultById(id);
                this.selectedRow = {
                    ...new SalaryResultObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new SalaryResultObject();
            }
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleClose = () => {
        this.openConfirmDeletePopup = false;
        this.openCreateEditPopup = false;
        this.openConfirmDeleteListPopup = false;
    };

    handleDelete = (row) => {
        this.selectedRow = {...row};
        this.openConfirmDeletePopup = true;
    };

    handleDeleteList = () => {
        this.openConfirmDeleteListPopup = true;
    };

    handleConfirmDelete = async () => {
        try {
            const {data} = await deleteSalaryResult(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingSalaryResult();
            this.handleClose();
            return data;
        } catch (error) {
            console.log(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleConfirmDeleteMultiple = async () => {
        try {
            const ids = this.selectedDataList.map((item) => item.id);
            await deleteMultipleSalaryResultByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingSalaryResult();
            this.handleClose();
        } catch (error) {
            console.log(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };

    handleSelectListDelete = (dataList) => {
        this.selectedDataList = dataList;
    };

    saveSalaryResult = async (data) => {
        try {
            await saveSalaryResult(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingSalaryResult();
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
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
