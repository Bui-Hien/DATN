import React, {memo, useEffect} from "react";
import {observer} from "mobx-react-lite";
import TabAccordion from "../../../common/Accordion/TabAccordion";
import Avatar from "@mui/material/Avatar";
import {API_ENDPOINT} from "../../../appConfig";
import FileUploadWithPreview from "../../../common/UploadFile/FileUploadWithPreview";
import BackupIcon from '@mui/icons-material/Backup';
import {Button} from "@mui/material";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../../stores";
import * as Yup from "yup";
import {useParams} from "react-router-dom";
import CommonTextField from "../../../common/form/CommonTextField";
import CommonSelectInput from "../../../common/form/CommonSelectInput";
import {Gender, MaritalStatus} from "../../../LocalConstants";
import {pagingCountry} from "../../Country/CountryService";
import CommonPagingAutocompleteV2 from "../../../common/form/CommonPagingAutocompleteV2";
import CommonDateTimePicker from "../../../common/form/CommonDateTimePicker";
import {pagingEthnics} from "../../Ethnics/EthnicsService";
import {pagingReligion} from "../../Religion/ReligionService";
import {pagingAdministrativeUnit} from "../../AdministrativeUnit/AdministrativeUnitService";

function PersonalInformation() {
    const {id} = useParams();
    const {t} = useTranslation();
    const [uploadedFile, setUploadedFile] = React.useState(false);
    const {staffStore} = useStore();

    const {
        saveStaff,
        selectedRow,
        getStaffById
    } = staffStore;

    const validationSchema = Yup.object({
        firstName: Yup.string()
            .trim()
            .required(t("validation.required")),
        lastName: Yup.string()
            .trim()
            .required(t("validation.required")),
        nationality: Yup.object()
            .nullable()
            .required(t("validation.required")),
        gender: Yup.number()
            .nullable()
            .required(t("validation.required")),
        email: Yup.string()
            .trim()
            .email(t("validation.invalid_email"))
            .required(t("validation.required")),
    });

    useEffect(() => {
        if (!id) return;
        getStaffById(id);
    }, [id]);

    async function handleSaveForm(values) {
        await saveStaff(values);
    }

    return (
        <Formik
            validationSchema={validationSchema}
            enableReinitialize
            initialValues={selectedRow}
            onSubmit={handleSaveForm}
        >
            {({isSubmitting, values, setFieldValue, initialValues}) => {
                return (
                    <Form autoComplete="off">
                        <div className="w-full grid gap-4 grid-cols-12 pt-5">
                            <div className="col-span-12">
                                <TabAccordion title='Thông tin cá nhân'>
                                    <div className="grid grid-cols-12 gap-2">
                                        <div
                                            className="col-span-12 md:col-span-4  flex flex-col justify-center items-center">
                                            <Avatar className={"!size-36 !text-6xl"}
                                                    src={`${API_ENDPOINT}/${values?.avatar?.filePath}`}
                                            >
                                                H
                                            </Avatar>
                                            <Button
                                                variant="contained"
                                                color="primary"
                                                className="!mt-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg px-4 py-2 shadow-md transition"
                                                startIcon={<BackupIcon/>}
                                                onClick={() => setUploadedFile(true)}
                                            >
                                                Chọn ảnh
                                            </Button>

                                            {uploadedFile && (
                                                <FileUploadWithPreview
                                                    open={uploadedFile}
                                                    handleClose={() => setUploadedFile(false)}
                                                    onUploadSuccess={(file) => {
                                                        setFieldValue("avatar", file);
                                                    }}
                                                />
                                            )}
                                        </div>
                                        <div className="grid grid-cols-12 gap-2 col-span-12 md:col-span-8">
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Họ")}
                                                    name="firstName"
                                                    required
                                                    onChange={(e) => {
                                                        const firstName = e.target.value;
                                                        setFieldValue("firstName", firstName);
                                                        setFieldValue("displayName", `${firstName.trim()} ${values.lastName || ""}`.trim());
                                                    }}
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Tên")}
                                                    name="lastName"
                                                    required
                                                    onChange={(e) => {
                                                        const lastName = e.target.value;
                                                        setFieldValue("lastName", lastName);
                                                        setFieldValue("displayName", `${values.firstName || ""} ${lastName.trim()}`.trim());
                                                    }}
                                                /></div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Họ và tên")}
                                                    name="displayName"
                                                    disabled
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonSelectInput
                                                    label={t("Giới tính")}
                                                    name="gender"
                                                    options={Gender.getListData()}
                                                    required
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonDateTimePicker
                                                    label={t("Ngày sinh")}
                                                    name="birthDate"
                                                    disableFuture
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Nơi sinh")}
                                                    name="birthPlace"
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Số điện thoại")}
                                                    name="phoneNumber"
                                                    type="number"
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("CCCD")}
                                                    name="idNumber"
                                                    type="number"
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Nơi cấp")}
                                                    name="idNumberIssueBy"
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonDateTimePicker
                                                    label={t("Ngày cấp")}
                                                    name="idNumberIssueDate"
                                                    disableFuture
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Email")}
                                                    name="email"
                                                    required
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Quốc tịch")}
                                                    name="nationality"
                                                    api={pagingCountry}
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Dân tộc")}
                                                    name="ethnics"
                                                    api={pagingEthnics}
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Tôn giáo")}
                                                    name="religion"
                                                    api={pagingReligion}
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonSelectInput
                                                    label={t("Tình trạng hôn nhân")}
                                                    name="maritalStatus"
                                                    options={MaritalStatus.getListData()}
                                                />
                                            </div>
                                            <div className="col-span-12 sm:col-span-6 md:col-span-4  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Mã số thuế")}
                                                    name="taxCode"
                                                />
                                            </div>
                                        </div>
                                        <div className="col-span-12 grid grid-cols-12 gap-2">
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonTextField
                                                    label={t("Chiều cao")}
                                                    name="height"
                                                    type="number"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonTextField
                                                    label={t("Cân nặng")}
                                                    name="weight"
                                                    type="number"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Tỉnh thường trú")}
                                                    name="permanentResidence.province"
                                                    api={pagingAdministrativeUnit}
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Huyện thường trú")}
                                                    name="permanentResidence.district"
                                                    api={pagingAdministrativeUnit}
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Xã thường trú")}
                                                    name="permanentResidence.ward"
                                                    api={pagingAdministrativeUnit}
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonTextField
                                                    label={t("Địa chỉ chi tiết")}
                                                    name="permanentResidence.addressDetail"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Tỉnh tạm trú")}
                                                    name="temporaryResidence.province"
                                                    api={pagingAdministrativeUnit}
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Huyện tạm trú")}
                                                    name="temporaryResidence.district"
                                                    api={pagingAdministrativeUnit}
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonPagingAutocompleteV2
                                                    label={t("Xã tạm trú")}
                                                    name="temporaryResidence.ward"
                                                    api={pagingAdministrativeUnit}
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-2">
                                                <CommonTextField
                                                    label={t("Địa chỉ chi tiết")}
                                                    name="temporaryResidence.addressDetail"
                                                />
                                            </div>
                                        </div>
                                    </div>
                                </TabAccordion>
                            </div>
                            <div className="col-span-12">
                                <TabAccordion title='Hồ sơ nhân viên'>
                                </TabAccordion>
                            </div>

                        </div>
                    </Form>
                );
            }}
        </Formik>

    );
}

export default memo(observer(PersonalInformation));
