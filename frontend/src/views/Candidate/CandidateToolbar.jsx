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
import {useNavigate} from "react-router-dom";
import CandidateFilter from "./CandidateFilter";
import FilterAltIcon from '@mui/icons-material/FilterAlt';
import RecordVoiceOverIcon from '@mui/icons-material/RecordVoiceOver';
import HowToRegIcon from '@mui/icons-material/HowToReg';
import FilterListIcon from "@mui/icons-material/FilterList";
import BlockIcon from "@mui/icons-material/Block";
import HighlightOffIcon from "@mui/icons-material/HighlightOff";
import {CandidateStatus} from "../../LocalConstants";

function CandidateToolbar() {
    const {candidateStore} = useStore();
    const navigate = useNavigate();

    const {t} = useTranslation();

    const {
        handleDeleteList,
        pagingCandidate,
        searchObject,
        selectedDataList,
        handleSetSearchObject,
        handleTogglePopupFilter,
        isOpenFilter,
        handleUpdateStatusList,
        handleUpdatePreScreenedList,
        currentTab
    } = candidateStore;

    async function handleFilter(values) {
        const newSearchObject = {
            ...values,
            pageIndex: 1,
        };
        handleSetSearchObject(newSearchObject);
        await pagingCandidate();
    }

    return (
        <Formik enableReinitialize initialValues={searchObject} onSubmit={handleFilter}>
            {({resetForm, values, setFieldValue, setValues}) => (
                <Form autoComplete="off" className="w-full">
                    <div className="grid grid-cols-1 gap-4 mb-4">
                        {/* Left buttons */}
                        <div className="col-span-1 space-x-2 flex justify-start">
                            <div className="flex justify-end">
                                <ButtonGroup
                                    color="container"
                                    aria-label="outlined primary button group"
                                >
                                    <Button
                                        onClick={() => navigate(`/candidate/create`)}
                                        startIcon={<AddIcon/>}
                                    >
                                        {t("general.button.add")}
                                    </Button>
                                    <Button
                                        onClick={handleUpdatePreScreenedList}
                                        startIcon={<FilterAltIcon/>}
                                        disabled={selectedDataList?.length <= 0}
                                    >
                                        {t("Lọc hồ sơ")}
                                    </Button>
                                    <Button
                                        onClick={() => {
                                            handleUpdateStatusList(CandidateStatus.INTERVIEWED.value)
                                        }}
                                        startIcon={<RecordVoiceOverIcon/>}
                                        disabled={selectedDataList?.length <= 0}
                                    >
                                        {t("Qua phỏng vấn")}
                                    </Button>
                                    <Button
                                        onClick={() => {
                                            handleUpdateStatusList(CandidateStatus.FAILED_INTERVIEWED.value)
                                        }}
                                        startIcon={<HighlightOffIcon/>}
                                        disabled={selectedDataList?.length <= 0}
                                    >
                                        {t("Trượt phỏng vấn")}
                                    </Button>
                                    <Button
                                        onClick={() => {
                                            handleUpdateStatusList(CandidateStatus.HIRED.value)
                                        }}
                                        startIcon={<HowToRegIcon/>}
                                        disabled={selectedDataList?.length <= 0 || currentTab === CandidateStatus.HIRED.value}
                                    >
                                        {t("Nhân việc")}
                                    </Button>
                                    <Button
                                        onClick={() => {
                                            handleUpdateStatusList(CandidateStatus.DECLINED.value)
                                        }}
                                        startIcon={<BlockIcon/>}
                                        disabled={selectedDataList?.length <= 0}
                                    >
                                        {t("Không nhận việc")}
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
                        <div className="grid col-span-1 grid-cols-12">
                            <div className="col-span-4"></div>
                            <div className="col-span-8 gap-2 flex justify-end">
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
                    </div>
                    <CandidateFilter/>
                </Form>
            )}
        </Formik>
    );
}

export default memo(observer(CandidateToolbar));
