import {makeAutoObservable} from "mobx";
import {deleteBank, deleteMultipleBankByIds, getBankById, pagingBank, saveBank,} from "./BankService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {BankObject} from "./Bank";

export default class BankStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new BankObject();
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

    pagingBank = async () => {
        try {
            const response = await pagingBank(this.searchObject);
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
                if (error?.response?.data?.message) {
                    toast.error(error?.response?.data?.message);
                } else {
                    toast.error(i18n.t("toast.error"));
                }
            }
        }
    };

    setPageIndex = async (page) => {
        this.searchObject.pageIndex = page;
        await this.pagingBank();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingBank();
    };
    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getBankById(id);
                this.selectedRow = {
                    ...new BankObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new BankObject();
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
            const {data} = await deleteBank(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingBank();
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
            await deleteMultipleBankByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingBank();
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

    saveBank = async (data) => {
        try {
            await saveBank(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingBank();
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
