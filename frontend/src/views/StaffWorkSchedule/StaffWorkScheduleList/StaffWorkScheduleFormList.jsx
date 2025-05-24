import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../../common/CommonPopupV2";
import {Button, Checkbox, DialogActions, DialogContent, FormControlLabel,} from "@mui/material";
import {observer} from "mobx-react-lite";
import SaveIcon from "@mui/icons-material/Save";
import CloseIcon from "@mui/icons-material/Close";
import {StaffWorkScheduleListObject} from "./StaffWorkScheduleListObject";
import {ShiftWorkType, Weekdays} from "../../../LocalConstants";
import i18next from "i18next";
import i18n from "i18next";
import CommonDateTimePicker from "../../../common/form/CommonDateTimePicker";
import StaffWorkScheduleListTable from "./StaffWorkScheduleListTable";
import {toast} from "react-toastify";

function StaffWorkScheduleForm() {
    const {t} = useTranslation();
    const {staffWorkScheduleStore} = useStore();
    const {
        handleClose, saveListStaffWorkSchedule, openCreateEditListPopup,
    } = staffWorkScheduleStore;

    const validationSchema = Yup.object({
        fromWorkingDate: Yup.date()
            .required(t("validation.required")),

        toWorkingDate: Yup.date()
            .required(t("validation.required"))
            .when("fromWorkingDate", (fromWorkingDate, schema) =>
                fromWorkingDate
                    ? schema.min(fromWorkingDate, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu")
                    : schema
            ),
    });


    async function handleSaveForm(values) {
        if (values.staffs.length === 0) {
            toast.error(i18n.t("Phải chọn ít nhất 1 nhân viên"));
            return;
        }
        if (values.shiftWorkTypeList.length === 0) {
            toast.error(i18n.t("Phải chọn ít nhất 1 ca làm việc"));
            return;
        }
        if (values.shiftWorkTypeList.length === 0) {
            toast.error(i18n.t("Phải chọn ít nhất 1 ca làm việc"));
            return;
        }
        if (values.weekdays.length === 0) {
            toast.error(i18n.t("Phải chọn ít nhất 1 ngày làm việc trong tuần"));
            return;
        }

        await saveListStaffWorkSchedule(values);
    }


    return (<CommonPopupV2
        size="md"
        scroll="body"
        open={openCreateEditListPopup}
        noDialogContent
        title={t("Phân ca làm việc")}
        onClosePopup={handleClose}
        isCreate
    >
        <Formik
            validationSchema={validationSchema}
            enableReinitialize
            initialValues={{...new StaffWorkScheduleListObject()}}
            onSubmit={handleSaveForm}
        >
            {({isSubmitting, values, setFieldValue}) => {
                const toggleShiftType = (value) => {
                    const updated = values.shiftWorkTypeList.includes(value) ? values.shiftWorkTypeList.filter((v) => v !== value) : [...values.shiftWorkTypeList, value];
                    setFieldValue("shiftWorkTypeList", updated);
                };

                const toggleWeekday = (value) => {
                    const updated = values.weekdays.includes(value) ? values.weekdays.filter((d) => d !== value) : [...values.weekdays, value];
                    setFieldValue("weekdays", updated);
                };

                return (<Form autoComplete="off">
                    <DialogContent className="p-6">
                        {/* CA LÀM VIỆC */}
                        <div className="grid grid-cols-12 gap-2">
                            <div className="col-span-12 border-b border-gray-300 pb-2 mb-2 font-semibold">
                                {t("Ca làm việc")}<span style={{color: "red"}}> * </span>
                            </div>
                            {ShiftWorkType.getListData().map((item) => (
                                <div key={item.value} className="col-span-12 md:col-span-4 lg:col-span-3">
                                    <FormControlLabel
                                        control={<Checkbox
                                            checked={values.shiftWorkTypeList.includes(item.value)}
                                            onChange={() => toggleShiftType(item.value)}
                                        />}
                                        label={item.name}
                                    />
                                </div>))}

                            {/* THỜI GIAN ÁP DỤNG */}
                            <div className="col-span-12 border-b border-gray-300 pb-2 mb-2 mt-4 font-semibold">
                                {t("Thời gian áp dụng")}
                            </div>
                            <div className="col-span-12 grid grid-cols-12 gap-8 items-end">
                                <div className="col-span-4 grid grid-cols-12 gap-2">
                                    <div className="col-span-12">
                                        <CommonDateTimePicker
                                            label={i18next.t("Ngày bắt đầu phân ca")}
                                            name="fromWorkingDate"
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonDateTimePicker
                                            label={i18next.t("Ngày kết thúc phân ca")}
                                            name="toWorkingDate"
                                            required
                                        />
                                    </div>
                                </div>

                                {/* CHỌN CÁC NGÀY TRONG TUẦN */}
                                <div className="col-span-8">
                                            <span className="font-bold text-gray-700">
                                                Ngày làm việc<span style={{color: "red"}}> * </span>:
                                            </span>
                                    <div className="border border-gray-300 bg-gray-100 rounded p-4">
                                        <div className="grid grid-cols-12 gap-2">
                                            {Weekdays.getListData().map((day) => (<div
                                                key={day.value}
                                                className="col-span-4 md:col-span-3 flex items-center"
                                            >
                                                <FormControlLabel
                                                    control={<Checkbox
                                                        checked={values.weekdays.includes(day.value)}
                                                        onChange={() => toggleWeekday(day.value)}
                                                    />}
                                                    label={day.name}
                                                />
                                            </div>))}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-span-12 ">
                                <StaffWorkScheduleListTable/>
                            </div>
                        </div>
                    </DialogContent>

                    <DialogActions className="px-6 pb-4">
                        <div className="flex justify-end w-full">
                            <Button
                                variant="outlined"
                                color="secondary"
                                disabled={isSubmitting}
                                onClick={handleClose}
                                className="rounded-lg px-4 py-2 !mr-2 !bg-red-500"
                                startIcon={<CloseIcon/>}
                            >
                                {t("general.button.close")}
                            </Button>
                            <Button
                                variant="contained"
                                color="primary"
                                type="submit"
                                disabled={isSubmitting}
                                className="rounded-lg px-4 py-2"
                                startIcon={<SaveIcon/>}
                            >
                                {t("general.button.save")}
                            </Button>
                        </div>
                    </DialogActions>
                </Form>);
            }}
        </Formik>
    </CommonPopupV2>);
}

export default memo(observer(StaffWorkScheduleForm));
