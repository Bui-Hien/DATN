import React, {memo} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button, DialogActions, DialogContent} from "@mui/material";
import CommonTextField from "../../common/form/CommonTextField";
import {observer} from "mobx-react-lite";
import SaveIcon from '@mui/icons-material/Save';
import CloseIcon from "@mui/icons-material/Close";
import {removeVietnameseTones} from "../../LocalFunction";
import {SalaryPeriodStatus} from "../../LocalConstants";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import CommonNumberInput from "../../common/form/CommonNumberInput";

function SalaryPeriodForm(props) {
    const {t} = useTranslation();
    const {salaryPeriodStore} = useStore();

    const {
        handleClose,
        saveSalaryPeriod,
        selectedRow,
        openCreateEditPopup,
    } = salaryPeriodStore;

    const validationSchema = Yup.object({
        code: Yup.string()
            .trim()
            .required(t("validation.required")),

        name: Yup.string()
            .trim()
            .nullable()
            .required(t("validation.required")),

        description: Yup.string().nullable(),


        startDate: Yup.date().nullable().required(t("validation.required")),


        endDate: Yup.date()
            .nullable()
            .min(
                Yup.ref('startDate'),
                t("validation.endDateMustBeAfterStartDate") // bạn cần thêm vào file i18n
            ).required(t("validation.required")),

        estimatedWorkingDays: Yup.number()
            .nullable()
            .required(t("validation.required"))
            .test(
                'max-working-days',
                t("validation.workingDaysExceed"),
                function (value) {
                    const {startDate, endDate} = this.parent;
                    if (!startDate || !endDate || value == null) return true;
                    const diffInMs = new Date(endDate) - new Date(startDate);
                    const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24)) + 1;
                    return value <= diffInDays;
                }
            )
    });


    async function handleSaveForm(values) {
        await saveSalaryPeriod(values);
    }

    const calculateWorkingDays = (startDate, endDate, setFieldValue) => {
        startDate = new Date(startDate);
        endDate = new Date(endDate);

        if (!(startDate instanceof Date) || isNaN(startDate) ||
            !(endDate instanceof Date) || isNaN(endDate)) {
            setFieldValue("estimatedWorkingDays", 0);
            return;
        }

        let count = 0;
        let current = new Date(startDate);
        current.setHours(0, 0, 0, 0);
        const end = new Date(endDate);
        end.setHours(0, 0, 0, 0);

        while (current <= end) {
            const day = current.getDay();
            if (day !== 0) {
                count++;
            }
            current.setDate(current.getDate() + 1);
        }

        setFieldValue("estimatedWorkingDays", count);
    };

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Kỳ lương")}
            onClosePopup={handleClose}
            isCreate={!selectedRow?.id}
        >
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={selectedRow}
                onSubmit={handleSaveForm}
            >
                {({isSubmitting, values, setFieldValue, initialValues}) => {
                    return (
                        <Form autoComplete="off">
                            <DialogContent className="p-6">
                                <div className={"grid grid-cols-12 gap-2"}>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonTextField
                                            label="Tên kỳ lương"
                                            name="name"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("name", nameValue);
                                                setFieldValue("code", codeValue);
                                            }}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonTextField
                                            label="Mã kỳ lương"
                                            name="code"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonDateTimePicker
                                            label={t("Ngày bắt đầu")}
                                            name="startDate"
                                            onChange={(startDate) => {
                                                setFieldValue("startDate", startDate);
                                                calculateWorkingDays(startDate, values?.endDate, setFieldValue);
                                            }}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonDateTimePicker
                                            label={t("Ngày kết thúc")}
                                            name="endDate"
                                            onChange={(endDate) => {
                                                setFieldValue("endDate", endDate);
                                                calculateWorkingDays(values?.startDate, endDate, setFieldValue);
                                            }}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonNumberInput
                                            label={t("Số ngày ước tính")}
                                            name="estimatedWorkingDays"
                                            required/>
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonSelectInput
                                            label={t("Trạng thái kỳ lương")}
                                            name={"salaryPeriodStatus"}
                                            options={SalaryPeriodStatus.getListData()}
                                            disabled={true}
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mô tả"
                                            name="description"
                                            multiline
                                            rows={3}
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
                }
                }
            </Formik>
        </CommonPopupV2>
    );
}

export default memo(observer(SalaryPeriodForm));
