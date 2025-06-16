import React, {memo, useEffect, useMemo, useRef, useState} from "react";
import {Form, Formik, useFormikContext} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button, DialogActions, DialogContent} from "@mui/material";
import {observer} from "mobx-react-lite";
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from "@mui/icons-material/Close";
import {ShiftWorkType, SystemRole} from "../../LocalConstants";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import ChooseSelectedStaff from "../../common/CommonSelectedStaff/ChooseSelectedStaff";
import {isSameDate} from "../../LocalFunction";
import {toast} from "react-toastify";

function TimeSheetDetailForm({isPerson = false}) {
    const {t} = useTranslation();
    const {staffWorkScheduleStore, staffStore, authStore} = useStore();
    const {
        handleClose,
        selectedRow,
        openCreateEditPopup,
        getListByStaffAndWorkingDate,
        listByStaffAndWorkingDate,
        markAttendance,
        handleSetSelectedRow,
        handleGetByStaffAndWorkingDateAndShiftWorkType
    } = staffWorkScheduleStore;

    const {
        hasRole,
        roles
    } = authStore;

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

    const [isEdit, setIsEdit] = useState(true);

    useEffect(() => {
            const innitData = async () => {
                await staffStore.getCurrentStaff()
                handleSetSelectedRow({
                    staff: staffStore.selectedRow,
                    workingDate: new Date(),
                })
            }
            if (isEdit && openCreateEditPopup) {
                innitData();
            }
            setIsEdit(!hasRole([SystemRole.ROLE_ADMIN, SystemRole.ROLE_MANAGER, SystemRole.ROLE_HR]))
        },
        [isEdit, roles, openCreateEditPopup, staffStore, handleSetSelectedRow])

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
                {({isSubmitting, values, setFieldValue, setValues}) => {
                    return (
                        <>
                            <FormikEffects
                                listByStaffAndWorkingDate={listByStaffAndWorkingDate}
                                getListByStaffAndWorkingDate={getListByStaffAndWorkingDate}
                                handleGetByStaffAndWorkingDateAndShiftWorkType={handleGetByStaffAndWorkingDateAndShiftWorkType}
                                setValues={setValues}
                            />
                            <Form autoComplete="off">
                                <DialogContent className="p-6">
                                    <div className="grid grid-cols-12 gap-2">
                                        <div className="col-span-12">
                                            <ChooseSelectedStaff
                                                name={"staff"}
                                                multiline={false}
                                                required
                                                disabled={isEdit}
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
                                                options={ShiftWorkType.getListData().filter(opt =>
                                                    listByStaffAndWorkingDate.map(item => item.shiftWorkType).includes(opt.value)
                                                )}
                                                disabled={
                                                    !values.staff?.id || !values.workingDate || listByStaffAndWorkingDate.length <= 1
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
                                                disabled={isEdit}
                                            />
                                        </div>
                                        <div className="col-span-12 md:col-span-6">
                                            <CommonDateTimePicker
                                                label={t("Thời gian chấm công ra")}
                                                name="checkOut"
                                                isDateTimePicker={true}
                                                disabled={isEdit}
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
                        </>
                    );
                }}
            </Formik>
        </CommonPopupV2>
    );
}

function FormikEffects({
                           listByStaffAndWorkingDate,
                           getListByStaffAndWorkingDate,
                           handleGetByStaffAndWorkingDateAndShiftWorkType,
                           setValues
                       }) {
    const {values} = useFormikContext();
    const previousValues = useRef({});

    // Helper function để so sánh dates an toàn
    const isSameDate = (date1, date2) => {
        if (!date1 && !date2) return true;
        if (!date1 || !date2) return false;

        // Convert to Date if needed
        const d1 = date1 instanceof Date ? date1 : new Date(date1);
        const d2 = date2 instanceof Date ? date2 : new Date(date2);

        // Check if dates are valid
        if (isNaN(d1.getTime()) || isNaN(d2.getTime())) return false;

        return d1.getTime() === d2.getTime();
    };

    // Effect 1: Gọi API khi staff hoặc workingDate thay đổi
    useEffect(() => {
        const currentStaffId = values.staff?.id;
        const currentWorkingDate = values.workingDate;

        const prevStaffId = previousValues.current?.staff?.id;
        const prevWorkingDate = previousValues.current?.workingDate;

        // Chỉ gọi API khi có thay đổi thực sự
        if (currentStaffId && currentWorkingDate &&
            (currentStaffId !== prevStaffId ||
                !isSameDate(currentWorkingDate, prevWorkingDate))) {

            getListByStaffAndWorkingDate({
                staffId: currentStaffId,
                workingDate: currentWorkingDate,
            });
        }

        // Cập nhật previousValues
        previousValues.current = {
            staff: values.staff,
            workingDate: values.workingDate,
            shiftWorkType: values.shiftWorkType
        };
    }, [values.staff?.id, values.workingDate]);

    // Effect 2: Auto-fill form khi chỉ có 1 shift
    useEffect(() => {
        if (listByStaffAndWorkingDate.length === 1 &&
            !values.id) { // Chỉ auto-fill khi đang tạo mới
            setValues(prev => ({
                ...prev,
                ...listByStaffAndWorkingDate[0],
                checkIn: listByStaffAndWorkingDate[0]?.checkIn || new Date(),
                checkOut: listByStaffAndWorkingDate[0]?.checkIn ? (listByStaffAndWorkingDate[0]?.checkOut || new Date()) : null
            }));
        }
    }, [listByStaffAndWorkingDate, setValues, values.id]);

    // Effect 3: Gọi API khi có đủ thông tin để lấy chi tiết shift
    useEffect(() => {
        const currentStaffId = values.staff?.id;
        const currentWorkingDate = values.workingDate;
        const currentShiftWorkType = values.shiftWorkType;

        const prevStaffId = previousValues.current?.staff?.id;
        const prevWorkingDate = previousValues.current?.workingDate;
        const prevShiftWorkType = previousValues.current?.shiftWorkType;

        // Chỉ gọi API khi có thay đổi thực sự và có đủ thông tin
        if (currentStaffId && currentWorkingDate && currentShiftWorkType &&
            (currentStaffId !== prevStaffId ||
                !isSameDate(currentWorkingDate, prevWorkingDate) ||
                currentShiftWorkType !== prevShiftWorkType)) {

            const response = handleGetByStaffAndWorkingDateAndShiftWorkType({
                staffId: currentStaffId,
                workingDate: currentWorkingDate,
                shiftWorkType: currentShiftWorkType,
            });
            setValues(prev => ({
                ...prev,
                ...response
            }));
        }
    }, [values.staff?.id, values.workingDate, values.shiftWorkType]);

    return null;
}

export default memo(observer(TimeSheetDetailForm));

