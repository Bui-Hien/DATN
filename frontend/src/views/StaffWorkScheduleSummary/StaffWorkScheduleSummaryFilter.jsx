import React, {memo} from 'react';
import {useFormikContext} from "formik";
import {useStore} from "../../stores";
import {observer} from "mobx-react-lite";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import {Button, ButtonGroup, Collapse} from "@mui/material";
import RotateLeftIcon from '@mui/icons-material/RotateLeft';
import {SearchObject} from "../StaffWorkSchedule/SearchObject";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {ShiftWorkType} from "../../LocalConstants";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {pagingStaff} from "../Staff/StaffService";
import {pagingDepartment} from "../Department/DepartmentService";
import i18next from "i18next";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";

function StaffWorkScheduleSummaryFilter() {
    const {staffWorkScheduleSummaryStore} = useStore();
    const {
        isOpenFilter, handleCloseFilter, getScheduleSummary, handleSetSearchObject,
    } = staffWorkScheduleSummaryStore;
    const {resetForm} = useFormikContext();

    async function handleResetFilter() {
        const newSearchObject = {
            ...JSON.parse(JSON.stringify(new SearchObject()))
        };
        resetForm();
        handleSetSearchObject(newSearchObject)
        await getScheduleSummary()
    }

    return (<Collapse in={isOpenFilter} className="filterPopup">
        <div className={"gap-2 grid grid-cols-12"}>
            <div className="grid grid-cols-12 gap-4 col-span-12">
                <div className="col-span-12 border-b border-gray-300 pb-2 mb-2">
                    Đối tượng được phân ca
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={"Phòng ban"}
                        name={"department"}
                        api={pagingDepartment}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={"Nhân viên được phân ca"}
                        name={"owner"}
                        api={pagingStaff}
                        getOptionLabel={(option) => option?.staffCode && option?.displayName ? `${option.staffCode} - ${option.displayName}` : option?.staffCode || option?.displayName || ""}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonSelectInput
                        label={i18next.t("Ca làm việc")}
                        name="shiftWorkType"
                        options={ShiftWorkType.getListData()}
                    />
                </div>
                <div className="col-span-12 border-b border-gray-300 pb-2 mb-2">
                    Thời gian phân ca
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonDateTimePicker
                        label={i18next.t("Từ ngày")}
                        name="fromDate"
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonDateTimePicker
                        label={i18next.t("Đến ngày")}
                        name="toDate"
                    />
                </div>
            </div>

            <div className="col-span-12 border-t border-gray-300">
                <div className="mt-4 flex justify-end">
                    <ButtonGroup
                        color="container"
                        aria-label="outlined primary button group"
                    >
                        <Button
                            onClick={handleResetFilter}
                            startIcon={<RotateLeftIcon/>}
                        >
                            Đặt lại
                        </Button>
                        <Button
                            type="button"
                            onClick={handleCloseFilter}
                            startIcon={<HighlightOffIcon/>}
                        >
                            Đóng bộ lọc
                        </Button>
                    </ButtonGroup>
                </div>
            </div>
        </div>
    </Collapse>);
}

export default memo(observer(StaffWorkScheduleSummaryFilter));