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
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {pagingDepartment} from "./DepartmentService";

function DepartmentForm() {
    const {t} = useTranslation();
    const {departmentStore} = useStore();

    const {
        handleClose,
        saveDepartment,
        selectedRow,
        openCreateEditPopup,
    } = departmentStore;

    const validationSchema = Yup.object({
        code: Yup.string().trim().required(t("validation.required")),
        name: Yup.string().trim().nullable().required(t("validation.required")),
        description: Yup.string().nullable(),
        parent: Yup.object().nullable(),
        staffManager: Yup.object().nullable(),
    });


    async function handleSaveForm(values) {
        await saveDepartment(values);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Phòng ban")}
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
                                            label="Mã phòng ban"
                                            name="code"
                                            required/>
                                    </div>
                                    <div className="sm:col-span-12">
                                        <CommonTextField
                                            label="Tên phòng ban"
                                            name="name"
                                            required/>
                                    </div>
                                    <div className="sm:col-span-12">
                                        <CommonTextField
                                            label="Mô tả phòng ban"
                                            name="description"
                                        />
                                    </div>
                                    <div className="sm:col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label={"Nhân viên quản lý"}
                                            name={"staffManager"}
                                            api={pagingDepartment}
                                        />
                                    </div>

                                    <div className="sm:col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label={"Trực thuộc phòng ban"}
                                            name={"parent"}
                                            api={pagingDepartment}
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

export default memo(observer(DepartmentForm));
