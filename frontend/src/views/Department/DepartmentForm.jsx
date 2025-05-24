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
import CommonSelectInputV2 from "../../common/form/CommonSelectInputV2";
import PositionSection from "./PositionSection";
import {removeVietnameseTones} from "../../LocalFunction";

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
        positionManager: Yup.object().nullable(),
        positions: Yup.array()
            .of(
                Yup.object({
                    code: Yup.string().trim().required(t("validation.required")),
                    name: Yup.string().trim().required(t("validation.required")),
                    description: Yup.string().nullable(),
                    staff: Yup.object().nullable(),
                    isMain: Yup.boolean().nullable(),
                }).nullable()
            )
            .nullable(),
    });


    async function handleSaveForm(values) {
        await saveDepartment(values);
    }

    return (
        <CommonPopupV2
            size="md"
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
                                    <div className="col-span-12 md:col-span-6 xl:col-span-4">
                                        <CommonTextField
                                            label="Tên phòng ban"
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
                                    <div className="col-span-12 md:col-span-6 xl:col-span-4">
                                        <CommonTextField
                                            label="Mã phòng ban"
                                            name="code"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="col-span-12 md:col-span-6 xl:col-span-4">
                                        <CommonSelectInputV2
                                            name="positionManager"
                                            label="Người phụ trách phòng ban"
                                            options={values?.positions}
                                            getOptionLabel={(option) => {
                                                const positionCode = option?.code;
                                                const staffName = option?.staff?.displayName;

                                                if (positionCode && staffName) {
                                                    return `${positionCode} - ${staffName}`;
                                                } else {
                                                    return positionCode || staffName || "";
                                                }
                                            }}
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6 xl:col-span-4">
                                        <CommonPagingAutocompleteV2
                                            label={"Trực thuộc phòng ban"}
                                            name={"parent"}
                                            api={pagingDepartment}
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mô tả phòng ban"
                                            name="description"
                                            multiline={true}
                                            rows={4}
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <PositionSection/>
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
    );
}

export default memo(observer(DepartmentForm));
