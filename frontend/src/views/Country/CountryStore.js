import {makeAutoObservable} from "mobx";
import {
    pagingCountry,
    getCountryById,
    saveCountry,
    deleteCountry,
    deleteMultipleCountryByIds,
} from "./CountryService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {CountryObject} from "./Country";

export default class CountryStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = null;
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

    pagingCountry = async () => {
        try {
            const data = await pagingCountry(this.searchObject);
            this.dataList = data.data.content;
            this.totalElements = data.data.totalElements;
            this.totalPages = data.data.totalPages;
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
        }
    };

    setPageIndex = async (page) => {
        this.searchObject.pageIndex = page;
        await this.pagingCountry();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingCountry();
    };

    handleChangePage = async (event, newPage) => {
        await this.setPageIndex(newPage);
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getCountryById(id);
                this.selectedRow = {
                    ...new CountryObject(),
                    ...data,
                };
            } else {
                this.selectedRow = new CountryObject();
            }
            this.openCreateEditPopup = true;
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
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
            const {data} = await deleteCountry(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingCountry();
            this.handleClose();
            return data;
        } catch (error) {
            console.log(error);
            toast.warning(i18n.t("toast.error"));
        }
    };

    handleConfirmDeleteMultiple = async () => {
        try {
            const ids = this.selectedDataList.map((item) => item.id);
            await deleteMultipleCountryByIds(ids);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingCountry();
            this.handleClose();
        } catch (error) {
            console.log(error);
            toast.warning(i18n.t("toast.error"));
        }
    };

    handleSelectListDelete = (dataList) => {
        this.selectedDataList = dataList;
    };

    saveCountry = async (data) => {
        try {
            await saveCountry(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingCountry();
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
        }
    };

    getCountry = async (id) => {
        if (id) {
            try {
                const {data} = await getCountryById(id);
                this.selectedRow = data;
                this.openCreateEditPopup = true;
            } catch (error) {
                console.log(error);
                toast.warning(i18n.t("toast.error"));
            }
        } else {
            this.selectedRow = {};
        }
    };

    handleSetSearchObject = (searchObject) => {
        this.searchObject = {...searchObject};
    };

    setOpenCreateEditPopup = (value) => {
        this.openCreateEditPopup = value;
    };

    setSelectedRow = (data) => {
        this.selectedRow = {
            ...data,
        };
    };

    handleOpenFilter = () => {
        this.isOpenFilter = true;
    };

    handleCloseFilter = () => {
        this.isOpenFilter = false;
    };

    handleTogglePopupFilter = () => {
        this.isOpenFilter = !this.isOpenFilter;
    };
}
