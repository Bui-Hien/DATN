import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import {Button, ButtonGroup} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import DeleteIcon from "@mui/icons-material/Delete";
import SearchIcon from "@mui/icons-material/Search";
import CommonTextField from "../../common/form/CommonTextField";
import {observer} from "mobx-react-lite";
import FilterListIcon from "@mui/icons-material/FilterList";
import StaffWorkScheduleFilter from "./StaffWorkScheduleFilter";
import AccessTimeIcon from '@mui/icons-material/AccessTime';

function StaffWorkScheduleToolbar() {
    const {staffWorkScheduleStore} = useStore();
    const {t} = useTranslation();

    const {
        handleDeleteList,
        pagingStaffWorkSchedule,
        handleOpenCreateEdit,
        searchObject,
        selectedDataList,
        handleSetSearchObject,
        handleTogglePopupFilter,
        isOpenFilter,
        handleOpenCreateEditListPopup
    } = staffWorkScheduleStore;

    async function handleFilter(values) {
        const newSearchObject = {
            ...values,
            pageIndex: 1,
        };
        handleSetSearchObject(newSearchObject);
        await pagingStaffWorkSchedule();
    }

    return (
        <Formik enableReinitialize initialValues={searchObject} onSubmit={handleFilter}>
            {({resetForm, values, setFieldValue, setValues}) => (
                <Form autoComplete="off" className="w-full">
                    <div className="flex flex-wrap items-center justify-between gap-4 mb-4">
                        {/* Left buttons */}
                        <div className="flex space-x-2">
                            <div className="flex justify-end">
                                <ButtonGroup
                                    color="container"
                                    aria-label="outlined primary button group"
                                >
                                    <Button
                                        onClick={handleOpenCreateEditListPopup}
                                        startIcon={<AccessTimeIcon/>}
                                    >
                                        {t("Phân ca")}
                                    </Button>
                                    <Button
                                        onClick={() => handleOpenCreateEdit(null)}
                                        startIcon={<AddIcon/>}
                                    >
                                        {t("general.button.add")}
                                    </Button>
                                    <Button
                                        type="button"
                                        onClick={handleDeleteList}
                                        startIcon={<DeleteIcon/>}
                                        disabled={selectedDataList?.length <= 0}
                                    >
                                        {t("general.button.delete") || "Xóa"}
                                    </Button>
                                </ButtonGroup>
                            </div>
                        </div>

                        {/* Right search + filter */}
                        <div className="flex items-center gap-2 flex-1 max-w-xl">
                            <CommonTextField
                                name="keyword"
                                placeholder={t("Nhập từ khóa tìm kiếm")}
                                className="flex-grow"
                            />
                            <div className="inline-flex rounded-md shadow-sm" role="group">
                                <ButtonGroup
                                    color="container"
                                    aria-label="outlined primary button group"
                                >
                                    <Button
                                        className="whitespace-nowrap"
                                        type="submit"
                                        startIcon={<SearchIcon/>}
                                    >
                                        {t("general.button.search") || "Tìm kiếm"}
                                    </Button>
                                    <Button
                                        className="whitespace-nowrap"
                                        onClick={handleTogglePopupFilter}
                                        startIcon={<FilterListIcon
                                            className={`w-5 h-5 transform transition-transform duration-300 ${isOpenFilter ? "rotate-180" : ""}`}
                                        />}
                                    >
                                        {t("general.button.filter") || "Bộ lọc"}
                                    </Button>
                                </ButtonGroup>
                            </div>
                        </div>
                    </div>
                    <StaffWorkScheduleFilter/>
                </Form>
            )}
        </Formik>
    );
}

export default memo(observer(StaffWorkScheduleToolbar));
