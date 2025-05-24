import React, {memo} from 'react';
import {useFormikContext} from "formik";
import {useStore} from "../../stores";
import {observer} from "mobx-react-lite";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import {Button, ButtonGroup, Collapse} from "@mui/material";
import RotateLeftIcon from '@mui/icons-material/RotateLeft';
import {SearchObject} from "./SearchObject";
import {pagingDepartment} from "../Department/DepartmentService";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";
import {EducationLevel, EmployeeStatus, Gender, MaritalStatus, StaffPhase} from "../../LocalConstants";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {useTranslation} from "react-i18next";

function StaffFilter() {
    const {t} = useTranslation();
    const {staffStore} = useStore();
    const {
        isOpenFilter,
        handleCloseFilter,
        pagingStaff,
        handleSetSearchObject,
    } = staffStore;
    const {resetForm} = useFormikContext();

    async function handleResetFilter() {
        const newSearchObject = {
            ...JSON.parse(JSON.stringify(new SearchObject())),
        };
        resetForm();
        handleSetSearchObject(newSearchObject)
        await pagingStaff()
    }

    return (<Collapse in={isOpenFilter} className="filterPopup">
        <div className={"gap-2 grid grid-cols-12"}>
            <div className="grid grid-cols-12 gap-4 col-span-12">
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonDateTimePicker
                        label={t("Ngày bắt đầu tuyển dụng")}
                        name="fromRecruitmentDate"
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonDateTimePicker
                        label={t("Ngày kết thúc tuyển dụng")}
                        name="toRecruitmentDate"
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonDateTimePicker
                        label={t("Ngày bắt đầu chính thức")}
                        name="fromStartDate"
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonDateTimePicker
                        label={t("Ngày kết thúc chính thức")}
                        name="toStartDate"
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonSelectInput
                        label={t("Trạng thái nhân viên")}
                        name="employeeStatus"
                        options={EmployeeStatus.getListData()}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonSelectInput
                        label={t("Loại nhân viên")}
                        name="staffPhase"
                        options={StaffPhase.getListData()}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonSelectInput
                        label={t("Giới tính")}
                        name="gender"
                        options={Gender.getListData()}
                        required
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonSelectInput
                        label={t("Tình trạng hôn nhân")}
                        name="maritalStatus"
                        options={MaritalStatus.getListData()}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonSelectInput
                        label={t("Trình độ học vấn")}
                        name="educationLevel"
                        options={EducationLevel.getListData()}
                    />
                </div>
                <div className="col-span-12 md:col-span-4 lg:col-span-3">
                    <CommonPagingAutocompleteV2
                        label={t("Phòng ban làm việc")}
                        name={"department"}
                        api={pagingDepartment}
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

export default memo(observer(StaffFilter));