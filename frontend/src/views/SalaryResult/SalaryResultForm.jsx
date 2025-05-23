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
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {pagingSalaryPeriod} from "../SalaryPeriod/SalaryPeriodService";
import {pagingSalaryTemplate} from "../SalaryTemplate/SalaryTemplateService";

function SalaryResultForm(props) {
    const {t} = useTranslation();
    const {salaryResultStore} = useStore();

    const {
        handleClose,
        saveSalaryResult,
        selectedRow,
        openCreateEditPopup,
    } = salaryResultStore;

    const validationSchema = Yup.object({
        salaryPeriod: Yup.object().required(t("validation.required")),
        salaryTemplate: Yup.object().required(t("validation.required")),
        name: Yup.string().required(t("validation.required")),
    });


    async function handleSaveForm(values) {
        await saveSalaryResult(values);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Bảng lương")}
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
                                    <div className="col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label="Kỳ lương"
                                            name="salaryPeriod"
                                            api={pagingSalaryPeriod}
                                            handleChange={(_, value) => {
                                                const salaryPeriodName = removeVietnameseTones(value?.name || "").toUpperCase().replace(/\s+/g, "_");
                                                const salaryTemplateName = removeVietnameseTones(values?.salaryTemplate?.name || "").toUpperCase().replace(/\s+/g, "_");

                                                setFieldValue("salaryPeriod", value);
                                                setFieldValue("name", salaryPeriodName + "_" + salaryTemplateName);
                                            }}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label="Mẫu bảng lương sử dụng"
                                            name="salaryTemplate"
                                            api={pagingSalaryTemplate}
                                            handleChange={(_, value) => {
                                                const salaryPeriodName = removeVietnameseTones(values?.salaryPeriod?.name || "").toUpperCase().replace(/\s+/g, "_");
                                                const salaryTemplateName = removeVietnameseTones(value?.name || "").toUpperCase().replace(/\s+/g, "_");

                                                setFieldValue("salaryTemplate", value);
                                                setFieldValue("name", salaryPeriodName + "_" + salaryTemplateName);
                                            }}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Tên bảng lương"
                                            name="name"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const value = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("name", value);
                                            }}
                                            required/>
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

export default memo(observer(SalaryResultForm));
