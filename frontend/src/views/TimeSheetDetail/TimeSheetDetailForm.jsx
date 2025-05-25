import React, {memo, useEffect, useMemo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button, DialogActions, DialogContent} from "@mui/material";
import {observer} from "mobx-react-lite";
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from "@mui/icons-material/Close";
import {ShiftWorkType} from "../../LocalConstants";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import ChooseSelectedStaff from "../../common/CommonSelectedStaff/ChooseSelectedStaff";
import {isSameDate} from "../../LocalFunction";
import {toast} from "react-toastify";

function TimeSheetDetailForm(props) {
    const {t} = useTranslation();
    const {staffWorkScheduleStore} = useStore();

    const {
        handleClose,
        selectedRow,
        openCreateEditPopup,
        getListByStaffAndWorkingDate,
        listByStaffAndWorkingDate,
        markAttendance
    } = staffWorkScheduleStore;

    const validationSchema = Yup.object({
        shiftWorkType: Yup.number().required(t("validation.required")),
        staff: Yup.object().required(t("validation.required")),
        workingDate: Yup.date().required(t("validation.required")),
        checkIn: Yup.date().required(t("validation.required")),
    });

    function findShift(shiftData, shiftWorkType, staffId, workingDate) {
        return shiftData.find((shift) => {
            const staff = shift.staff || {};

            const matchStaff = !staffId || (staff.id && staff.id === staffId);
            const matchShiftWorkType = !shiftWorkType || shiftWorkType === shift.shiftWorkType;

            const matchDate = isSameDate(workingDate, shift.workingDate);

            return matchStaff && matchDate && matchShiftWorkType;
        }) || null;
    }

    async function handleSaveForm(values) {
        const shiftWork = findShift(listByStaffAndWorkingDate, values.shiftWorkType, values.staff?.id, values.workingDate);
        if (!shiftWork) {
            toast.error("Không tìm thấy ca làm việc của nhân viên")
        }
        const newValue = {
            ...shiftWork,
            checkIn: values.checkIn || null,
            checkOut: values.checkOut || null,
        }
        await markAttendance(newValue)
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Chấm công")}
            onClosePopup={handleClose}
            isCreate={!selectedRow?.id}
        >
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={selectedRow}
                onSubmit={handleSaveForm}
            >
                {({isSubmitting, values, setFieldValue}) => {
                    // eslint-disable-next-line react-hooks/rules-of-hooks
                    useEffect(() => {
                        if (values.staff?.id && values.workingDate) {
                            getListByStaffAndWorkingDate({
                                staffId: values.staff.id,
                                workingDate: values.workingDate,
                            });
                        }
                    }, [values.staff, values.workingDate]);

                    // Lọc danh sách ca làm việc chưa được chọn
                    // eslint-disable-next-line react-hooks/rules-of-hooks
                    const filteredShiftWorkOptions = useMemo(() => {
                        // Lấy danh sách các shiftWorkType đã được sử dụng
                        const usedShiftIds = listByStaffAndWorkingDate.map(item => item.shiftWorkType);
                        // Lấy tất cả các ca làm việc
                        const allOptions = ShiftWorkType.getListData();
                        // Lọc ra các ca làm việc
                        return allOptions.filter(opt => usedShiftIds.includes(opt.value));
                    }, [listByStaffAndWorkingDate, values?.id]);


                    return (
                        <Form autoComplete="off">
                            <DialogContent className="p-6">
                                <div className="grid grid-cols-12 gap-2">
                                    <div className="col-span-12">
                                        <ChooseSelectedStaff
                                            name={"staff"}
                                            multiline={false}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonDateTimePicker
                                            label={t("Ngày làm việc")}
                                            name="workingDate"
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonSelectInput
                                            label={t("Ca làm việc")}
                                            name="shiftWorkType"
                                            options={filteredShiftWorkOptions}
                                            disabled={
                                                !values.staff?.id || !values.workingDate
                                            }
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonDateTimePicker
                                            label={t("Thời gian chấm công vào")}
                                            name="checkIn"
                                            isDateTimePicker={true}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonDateTimePicker
                                            label={t("Thời gian chấm công ra")}
                                            name="checkOut"
                                            isDateTimePicker={true}
                                        />
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
                        </Form>
                    );
                }}
            </Formik>
        </CommonPopupV2>
    )
        ;
}

export default memo(observer(TimeSheetDetailForm));
