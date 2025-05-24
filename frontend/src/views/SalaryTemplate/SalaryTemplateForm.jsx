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
import SalaryTemplateItemSection from "./SalaryTemplateItemSection";
import {removeVietnameseTones} from "../../LocalFunction";

function SalaryTemplateForm(props) {
    const {t} = useTranslation();
    const {salaryTemplateStore} = useStore();

    const {
        handleClose,
        saveSalaryTemplate,
        selectedRow,
        openCreateEditPopup,
    } = salaryTemplateStore;

    const validationSchema = Yup.object({
        name: Yup.string().trim().required(t("validation.required")),
        code: Yup.string().trim().required(t("validation.required")),
        description: Yup.string().nullable(),
        formula: Yup.string().nullable(),
        templateItems: Yup.array().of(
            Yup.object({
                name: Yup.string().trim().required(t("validation.required")),
                code: Yup.string().trim().required(t("validation.required")),
                displayOrder: Yup.number().nullable().required(t("validation.required")),
                salaryItemType: Yup.number().nullable().required(t("validation.required")),
                defaultAmount: Yup.number().nullable(),
                formula: Yup.string().nullable(),
            }).nullable()
        ),
    });


    async function handleSaveForm(values) {


        await saveSalaryTemplate(values);
    }


    return (
        <CommonPopupV2
            size="xl"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Mẫu bảng lương")}
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
                                    <div className="sm:col-span-12">
                                        <CommonTextField
                                            label="Tên mẫu bảng lương"
                                            name="name"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("name", nameValue);
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="sm:col-span-12">
                                        <CommonTextField
                                            label="Mã mẫu bảng lương"
                                            name="code"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="sm:col-span-12">
                                        <CommonTextField
                                            label="Mô tả mẫu bảng lương"
                                            name="description"
                                            multiline
                                            rows={3}
                                        />
                                    </div>
                                    <div className="sm:col-span-12">
                                        <SalaryTemplateItemSection/>
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

export default memo(observer(SalaryTemplateForm));
