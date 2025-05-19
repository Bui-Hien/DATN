import {makeAutoObservable} from "mobx";
import {
    pagingProfession,
    getProfessionById,
    saveProfession,
    deleteProfession,
    deleteMultipleProfessionByIds,
} from "./ProfessionService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {ProfessionObject} from "./Profession";

export default class ProfessionStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new ProfessionObject();
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

    pagingProfession = async () => {
        try {
            const response = await pagingProfession(this.searchObject);
            const result = response.data;
            this.dataList = result.data.content || [];
            this.totalElements = result.data.totalElements || 0;
            this.totalPages = result.data.totalPages || 0;
            console.log(this.dataList)
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
        }
    };

    setPageIndex = async (page) => {
        this.searchObject.pageIndex = page;
        await this.pagingProfession();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingProfession();
    };

    handleChangePage = async (event, newPage) => {
        await this.setPageIndex(newPage);
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getProfessionById(id);
                this.selectedRow = {
                    ...new ProfessionObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new ProfessionObject();
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
            const {data} = await deleteProfession(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingProfession();
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
            await deleteMultipleProfessionByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingProfession();
            this.handleClose();
        } catch (error) {
            console.log(error);
            toast.warning(i18n.t("toast.error"));
        }
    };

    handleSelectListDelete = (dataList) => {
        this.selectedDataList = dataList;
    };

    saveProfession = async (data) => {
        try {
            await saveProfession(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingProfession();
        } catch (error) {
            console.error(error);
            toast.error(i18n.t("toast.error"));
        }
    };

    getBank = async (id) => {
        if (id) {
            try {
                const {data} = await getProfessionById(id);
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
