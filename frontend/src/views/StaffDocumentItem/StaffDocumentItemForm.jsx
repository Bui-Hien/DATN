import React, {memo, useState} from "react";
import {FieldArray, Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button} from "@mui/material";
import {observer} from "mobx-react-lite";
import SaveIcon from "@mui/icons-material/Save";
import CloseIcon from "@mui/icons-material/Close";
import {API_ENDPOINT} from "../../appConfig";
import VisibilityIcon from "@mui/icons-material/Visibility";
import DeleteIcon from "@mui/icons-material/Delete";
import FileUploadWithPreview from "../../common/UploadFile/FileUploadWithPreview";
import BackupIcon from "@mui/icons-material/Backup";
import handleDownload from "../../common/UploadFile/DowloadFile";
import FileDownloadIcon from "@mui/icons-material/FileDownload";
import {getDate} from "../../LocalFunction";

function StaffDocumentItemForm({handleClose}) {
    const {t} = useTranslation();
    const {staffDocumentItemStore} = useStore();
    const {saveListStaffDocumentItem, dataList} = staffDocumentItemStore;

    const [uploadIndex, setUploadIndex] = useState(null); // index đang upload
    const [previewFile, setPreviewFile] = useState(null); // file đang preview

    const validationSchema = Yup.object({});

    async function handleSaveForm(values) {
        await saveListStaffDocumentItem(values.staffDocumentItems);
    }

    return (
        <Formik
            validationSchema={validationSchema}
            enableReinitialize
            initialValues={{staffDocumentItems: dataList}}
            onSubmit={handleSaveForm}
        >
            {({isSubmitting, values, setFieldValue, resetForm}) => (
                <Form autoComplete="off" className="min-h-screen flex flex-col justify-between relative">
                    <div className="grid grid-cols-12 gap-2">
                        <div className="col-span-12">
                            <FieldArray name="staffDocumentItems">
                                {() => (
                                    <div className="mt-4 overflow-x-auto rounded-lg border border-gray-300 shadow">
                                        <table className="w-full table-auto border-collapse">
                                            <thead>
                                            <tr className="bg-gray-100 text-sm text-gray-700">
                                                <th className="border border-gray-300 px-4 py-2 text-center rounded-tl-md">{t("STT")}</th>
                                                <th className="border border-gray-300 px-4 py-2 text-left">{t("Tên tài liệu")}</th>
                                                <th className="border border-gray-300 px-4 py-2 text-center">{t("Tải lên / Xem / Xóa")}</th>
                                                <th className="border border-gray-300 px-4 py-2 text-center">{t("Ngày nộp")}</th>
                                                <th className="border border-gray-300 px-4 py-2 text-center rounded-tr-md">{t("Bắt buộc hay không")}</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            {values.staffDocumentItems?.length > 0 ? (
                                                values.staffDocumentItems.map((item, index) => (
                                                    <StaffDocumentItem
                                                        key={index}
                                                        index={index}
                                                        nameSpace={`staffDocumentItems[${index}]`}
                                                        file={item.documentFile}
                                                        documentItem={item.documentItem}
                                                        onUploadClick={() => setUploadIndex(index)}
                                                        onPreviewClick={() => setPreviewFile(item.documentFile)}
                                                        onDeleteFile={() => {
                                                            setFieldValue(`staffDocumentItems[${index}].documentFile`, null);
                                                        }}
                                                        onDownloadFile={() => {
                                                            if (item.documentFile?.id) {
                                                                handleDownload(item.documentFile);
                                                            }
                                                        }}
                                                    />
                                                ))
                                            ) : (
                                                <tr>
                                                    <td colSpan={5}
                                                        className="border border-gray-300 px-4 py-4 text-center text-gray-500">
                                                        {t("Chưa có phần tử nào")}
                                                    </td>
                                                </tr>
                                            )}
                                            </tbody>
                                        </table>
                                    </div>
                                )}
                            </FieldArray>
                        </div>
                    </div>

                    {/* Popup Tải lên */}
                    {uploadIndex !== null && (
                        <FileUploadWithPreview
                            open={true}
                            handleClose={() => setUploadIndex(null)}
                            onUploadSuccess={(fileInfo) => {
                                setFieldValue(`staffDocumentItems[${uploadIndex}].documentFile`, fileInfo);
                                setUploadIndex(null);
                            }}
                        />
                    )}

                    {/* Popup Preview */}
                    {previewFile && (
                        <CommonPopupV2
                            size="xs"
                            scroll={"paper"}
                            open={previewFile}
                            noDialogContent
                            title={"Tài liệu vừa tải lên"}
                            onClosePopup={() => setPreviewFile(null)}
                            noIcon={true}
                        >
                            <div className="p-4 text-center">
                                <img
                                    src={`${API_ENDPOINT}/${previewFile.filePath}`}
                                    alt="document"
                                    className="max-w-full max-h-[500px] object-contain rounded-md shadow-lg mx-auto"
                                />
                            </div>
                        </CommonPopupV2>
                    )}

                    <div className="w-full sticky bottom-0 bg-white border-t mt-4 py-3 z-10">
                        <div className="flex justify-end w-full">
                            <Button
                                variant="outlined"
                                color="secondary"
                                disabled={isSubmitting}
                                className="rounded-lg px-4 py-2 !mr-2 !bg-red-500"
                                startIcon={<CloseIcon/>}
                                onClick={resetForm}
                            >
                                {t("Hủy")}
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
                    </div>
                </Form>
            )}
        </Formik>
    );
}

export default memo(observer(StaffDocumentItemForm));

const StaffDocumentItem = memo(({
                                    index,
                                    file,
                                    documentItem,
                                    onUploadClick,
                                    onPreviewClick,
                                    onDeleteFile,
                                    onDownloadFile
                                }) => {
    const {t} = useTranslation();

    return (
        <tr className="hover:bg-gray-50 transition">
            <td className="border border-gray-300 px-4 py-2 text-center text-sm">{index + 1}</td>
            <td className="border border-gray-300 px-4 py-2 text-sm">{documentItem?.name || ""}</td>
            <td className="border border-gray-300 px-4 py-2">
                <div className="flex justify-center items-center gap-2">
                    <BackupIcon
                        onClick={onUploadClick}
                        className="text-blue-600 cursor-pointer hover:text-blue-800"
                    />
                    {file?.filePath && (
                        <>
                            <DeleteIcon
                                onClick={onDeleteFile}
                                className="text-red-500 cursor-pointer hover:text-red-700"
                            />
                            <VisibilityIcon
                                onClick={onPreviewClick}
                                className="text-gray-600 cursor-pointer hover:text-gray-800"
                            />
                            <FileDownloadIcon
                                onClick={onDownloadFile}
                                className={`${
                                    file?.id
                                        ? 'text-green-600 cursor-pointer hover:text-green-800'
                                        : 'text-gray-400 cursor-not-allowed'
                                }`}
                            />
                        </>
                    )}
                </div>
            </td>
            <td className="border border-gray-300 px-4 py-2 text-center text-sm">
                {getDate(file?.updatedAt)}
            </td>
            <td className="border border-gray-300 px-4 py-2 text-center text-sm">
                {documentItem?.isRequired ? t("Có") : t("Không")}
            </td>
        </tr>
    );
});
