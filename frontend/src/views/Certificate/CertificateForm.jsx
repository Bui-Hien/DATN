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
import BackupIcon from "@mui/icons-material/Backup";
import FileUploadWithPreview from "../../common/UploadFile/FileUploadWithPreview";
import {API_ENDPOINT} from "../../appConfig";
import VisibilityIcon from '@mui/icons-material/Visibility';
import DeleteIcon from '@mui/icons-material/Delete';

function CertificateForm(props) {
    const {t} = useTranslation();
    const {certificateStore, staffStore} = useStore();
    const [uploadedFile, setUploadedFile] = React.useState(false);
    const [preview, setPreview] = React.useState(false);

    const {
        handleClose,
        saveCertificate,
        selectedRow,
        openCreateEditPopup,
    } = certificateStore;


    const validationSchema = Yup.object({
        code: Yup.string().required(t("validation.required")),
        name: Yup.string().required(t("validation.required")),
        description: Yup.string().nullable(),
        certificateFile: Yup.object().nullable(),
    });


    async function handleSaveForm(values) {
        if (!staffStore.selectedRow?.id) return
        const newValues = {
            ...values,
            person: staffStore.selectedRow
        }
        await saveCertificate(newValues);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Quan hệ nhân thân")}
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
                                        <CommonTextField
                                            label="Tên chứng chỉ"
                                            name="name"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("name", nameValue);
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mã chứng chỉ"
                                            name="code"
                                            onChange={(e) => {
                                                const nameValue = e.target.value;
                                                const codeValue = removeVietnameseTones(nameValue).toUpperCase().replace(/\s+/g, "_");
                                                setFieldValue("code", codeValue);
                                            }}
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <Button
                                            variant="contained"
                                            color="primary"
                                            className="!mt-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg px-4 py-2 shadow-md transition"
                                            startIcon={<BackupIcon/>}
                                            onClick={() => setUploadedFile(true)}
                                        >
                                            {t("Thêm chứng chỉ")}
                                        </Button>
                                        {values?.certificateFile?.id && (
                                            <Button
                                                variant="outlined"
                                                color="primary"
                                                className="!mt-3 !ml-3 rounded-lg px-4 py-2"
                                                startIcon={<VisibilityIcon/>}
                                                onClick={() => setPreview(true)}
                                            >
                                                {t("Xem chứng chỉ")}
                                            </Button>
                                        )}
                                        {values?.certificateFile?.id && (
                                            <Button
                                                variant="outlined"
                                                color="error"
                                                className="!mt-3 !ml-3 rounded-lg px-4 py-2"
                                                startIcon={<DeleteIcon className="text-red-600"/>}
                                                onClick={() => setFieldValue("certificateFile", null)}
                                            >
                                                {t("Xóa chứng chỉ")}
                                            </Button>
                                        )}
                                        {uploadedFile && (
                                            <FileUploadWithPreview
                                                open={uploadedFile}
                                                handleClose={() => setUploadedFile(false)}
                                                onUploadSuccess={(file) => {
                                                    setFieldValue("certificateFile", file);
                                                }}
                                            />
                                        )}
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mô tả chứng chỉ"
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
                            <CommonPopupV2
                                size="xs"
                                scroll={"paper"}
                                open={preview}
                                noDialogContent
                                title={"Chứng chỉ vừa tải lên"}
                                onClosePopup={() => setPreview(false)}
                                noIcon={true}
                            >
                                <div className="p-4 text-center">
                                    <img
                                        src={`${API_ENDPOINT}/${values?.certificateFile?.filePath}`}
                                        alt="certificate"
                                        className="max-w-full max-h-[500px] object-contain rounded-md shadow-lg mx-auto"
                                    />
                                </div>
                            </CommonPopupV2>

                        </Form>
                    );
                }
                }
            </Formik>
        </CommonPopupV2>
    );
}

export default memo(observer(CertificateForm));
