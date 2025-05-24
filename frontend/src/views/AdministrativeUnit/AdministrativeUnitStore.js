import {makeAutoObservable} from "mobx";
import {
    deleteAdministrativeUnit,
    deleteMultipleAdministrativeUnitByIds, exportExcelAdministrativeUnit,
    getAdministrativeUnitById, importExcelAdministrativeUnit,
    pagingTreeAdministrativeUnit,
    saveAdministrativeUnit,
} from "./AdministrativeUnitService";
import {toast} from "react-toastify";
import i18n from "i18next";
import {SearchObject} from "./SearchObject";
import {AdministrativeUnitObject} from "./AdministrativeUnit";
import {saveAs} from "file-saver";

export default class AdministrativeUnitStore {
    searchObject = JSON.parse(JSON.stringify(new SearchObject()));

    totalElements = 0;
    totalPages = 0;
    dataList = [];
    openConfirmDeletePopup = false;
    openConfirmDeleteListPopup = false;
    openCreateEditPopup = false;
    selectedRow = new AdministrativeUnitObject();
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

    pagingTreeAdministrativeUnit = async () => {
        try {
            const newSearch = {
                ...this.searchObject,
                provinceId: this.searchObject?.province?.id,
                districtId: this.searchObject?.district?.id,
                wardId: this.searchObject?.ward?.id,
                province: null,
                district: null,
                ward: null,

            }
            const response = await pagingTreeAdministrativeUnit(newSearch);
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
        await this.pagingTreeAdministrativeUnit();
    };

    setPageSize = async (size) => {
        this.searchObject.pageSize = size;
        this.searchObject.pageIndex = 1;
        await this.pagingTreeAdministrativeUnit();
    };

    handleOpenCreateEdit = async (id) => {
        try {
            if (id) {
                const {data} = await getAdministrativeUnitById(id);
                this.selectedRow = {
                    ...new AdministrativeUnitObject(),
                    ...data.data,
                };
            } else {
                this.selectedRow = new AdministrativeUnitObject();
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
            const {data} = await deleteAdministrativeUnit(this.selectedRow.id);
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingTreeAdministrativeUnit();
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
            await deleteMultipleAdministrativeUnitByIds(ids);
            this.selectedDataList = [];
            toast.success(i18n.t("toast.delete_success"));
            await this.pagingTreeAdministrativeUnit();
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

    saveAdministrativeUnit = async (data) => {
        try {
            await saveAdministrativeUnit(data);
            toast.success(i18n.t("toast.save_success"));
            this.handleClose();
            await this.pagingTreeAdministrativeUnit();
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.error"));
            }
        }
    };
    handleExportExcelAdministrativeUnit = async () => {
        debugger
        if (this.totalElements > 0) {
            try {
                const res = await exportExcelAdministrativeUnit({...this.searchObject});
                if (res && res.data) {
                    const blob = new Blob([res.data], {
                        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    });

                    saveAs(blob, "DuLieuDonViHanhChinh.xlsx");
                    toast.success(i18n.t("toast.successExport"));
                } else {
                    toast.error(i18n.t("toast.errorExport"));
                }
            } catch (error) {
                console.error("Export error:", error);
                toast.error(i18n.t("toast.errorExport"));
            }
        } else {
            toast.warning(i18n.t("toast.noData"));
        }
    };

    handleImportAdministrativeUnit = async (event) => {
        const fileInput = event.target; // Lưu lại trước
        const file = fileInput.files[0];

        try {
            await importExcelAdministrativeUnit(file)
            toast.success(i18n.t("toast.successImport"));
            this.searchObject = {
                ...this.searchObject,
                pageIndex: 1
            }
            await this.pagingTreeAdministrativeUnit()
        } catch (error) {
            console.error(error);
            if (error?.response?.data?.message) {
                toast.error(error?.response?.data?.message);
            } else {
                toast.error(i18n.t("toast.successExport"));
            }
        } finally {
            this.handleClose();
            fileInput.value = null;
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
