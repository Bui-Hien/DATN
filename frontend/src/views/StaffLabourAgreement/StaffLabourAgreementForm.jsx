import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import {observer} from "mobx-react-lite";
import {
    Button,
    DialogActions,
    DialogContent
} from "@mui/material";

import SaveIcon from "@mui/icons-material/Save";
import CloseIcon from "@mui/icons-material/Close";

import CommonPopupV2 from "../../common/CommonPopupV2";
import CommonTextField from "../../common/form/CommonTextField";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import CommonNumberInput from "../../common/form/CommonNumberInput";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";

import {ContractType, StaffLabourAgreementStatus} from "../../LocalConstants";

function StaffLabourAgreementForm() {
    const {t} = useTranslation();
    const {staffLabourAgreementStore, staffStore} = useStore();

    const {
        handleClose,
        saveStaffLabourAgreement,
        selectedRow,
        openCreateEditPopup
    } = staffLabourAgreementStore;

    // Validation schema for the form
    const validationSchema = Yup.object({
        staff: Yup.object().nullable(),
        contractType: Yup.number().required(t("validation.required")),
        labourAgreementNumber: Yup.string().required(t("validation.required")),
        startDate: Yup.date().nullable().required(t("validation.required")),
        durationMonths: Yup.number().nullable(),
        workingHour: Yup.number().nullable().required(t("validation.required")),
        workingHourWeekMin: Yup.number().nullable().required(t("validation.required")),
        salary: Yup.number().nullable().required(t("validation.required")),
        signedDate: Yup.date().nullable(),
        agreementStatus: Yup.number().nullable().required(t("validation.required")),
    });

    // Form submission handler
    const handleSaveForm = async (values) => {
        if (!staffStore.selectedRow?.id) return;

        const newValues = {
            ...values,
            staff: staffStore.selectedRow,
        };

        await saveStaffLabourAgreement(newValues);
    };

    return (
        <CommonPopupV2
            size="md"
            scroll="body"
            open={openCreateEditPopup}
            noDialogContent
            title={
                (selectedRow?.id ? t("general.button.edit") : t("general.button.add")) +
                " " +
                t("Quan hệ nhân thân")
            }
            onClosePopup={handleClose}
            isCreate={!selectedRow?.id}
        >
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={selectedRow}
                onSubmit={handleSaveForm}
            >
                {({isSubmitting}) => (
                    <Form autoComplete="off">
                        <DialogContent className="p-6">
                            <div className="grid grid-cols-12 gap-2">
                                <div className="col-span-12 md:col-span-4">
                                    <CommonTextField
                                        label={t("Số hợp đồng")}
                                        name="labourAgreementNumber"
                                        required
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}
                                    />
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonSelectInput
                                        label={t("Loại hợp đồng")}
                                        name="contractType"
                                        options={ContractType.getListData()}
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}
                                    />
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonDateTimePicker
                                        label={t("Ngày ký")}
                                        name="startDate"
                                        disableFuture
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}
                                    />
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonNumberInput
                                        label={t("Số tháng hợp đồng")}
                                        name="durationMonths"
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}
                                    />
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonNumberInput
                                        label={t("Số giờ làm việc tối thiểu trong ngày")}
                                        name="workingHour"
                                        required
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}
                                    />
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonNumberInput
                                        label={t("Số giờ làm việc tối thiểu trong tháng")}
                                        name="workingHourWeekMin"
                                        required
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}
                                    />
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonNumberInput
                                        label={t("Lương cơ bản")}
                                        name="salary"
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}
                                        required/>
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonDateTimePicker
                                        label={t("Ngày hết hạn")}
                                        name="signedDate"
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile}/>
                                </div>
                                <div className="col-span-12 md:col-span-4">
                                    <CommonSelectInput
                                        label={t("Trạng thái hợp đồng")}
                                        name="agreementStatus"
                                        options={StaffLabourAgreementStatus.getListData()}
                                        disabled={staffStore.isProfile}
                                        readOnly={staffStore.isProfile} required/>
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
                                    className={`rounded-lg px-4 py-2 ${!staffStore.isProfile && " !mr-2 "}  !bg-red-500`}
                                    startIcon={<CloseIcon/>}
                                >
                                    {t("general.button.close")}
                                </Button>
                                {!staffStore.isProfile && (
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
                                )}
                            </div>
                        </DialogActions>
                    </Form>
                )}
            </Formik>
        </CommonPopupV2>
    );
}

export default memo(observer(StaffLabourAgreementForm));