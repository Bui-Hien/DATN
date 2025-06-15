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
import {pagingPosition} from "../Position/PositionService";

function RecruitmentRequestForm(props) {
    const {t} = useTranslation();
    const {recruitmentRequestStore} = useStore();

    const {
        handleClose,
        saveRecruitmentRequest,
        selectedRow,
        openCreateEditPopup,
    } = recruitmentRequestStore;

    const validationSchema = Yup.object({
        code: Yup.string()
            .trim()
            .nullable()
            .required(t("validation.required")),

        position: Yup.object()
            .nullable()
            .required(t("validation.required")),

        name: Yup.string()
            .trim()
            .nullable()
            .required(t("validation.required")),

        description: Yup.string()
            .trim()
            .nullable()
            .required(t("validation.required")),

        request: Yup.string()
            .trim()
            .nullable()
            .required(t("validation.required")),
    });


    async function handleSaveForm(values) {
        await saveRecruitmentRequest(values);
    }

    return (
        <CommonPopupV2
            size="md"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Yêu cầu tuyển dụng")}
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
                                            label="Tên yêu cầu tuyển dụng"
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
                                            label="Mã yêu cầu tuyển dụng"
                                            name="code"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonPagingAutocompleteV2
                                            label="Vị trí ứng tuyển"
                                            name="position"
                                            api={pagingPosition}
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mô tả công việc"
                                            name="description"
                                            multiline
                                            rows={3}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Yêu cầu công việc"
                                            name="request"
                                            multiline
                                            rows={5}
                                            required
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

export default memo(observer(RecruitmentRequestForm));
