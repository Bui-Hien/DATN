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
import {useNavigate, useParams} from "react-router-dom";
import CommonTextField from "../../../common/form/CommonTextField";
import CommonSelectInput from "../../../common/form/CommonSelectInput";
import {EducationLevel, EmployeeStatus, Gender, MaritalStatus, StaffPhase} from "../../../LocalConstants";
import CommonPagingAutocompleteV2 from "../../../common/form/CommonPagingAutocompleteV2";
import CommonDateTimePicker from "../../../common/form/CommonDateTimePicker";
import {calculateDateDifference} from "../../../LocalFunction";
import CommonCheckBox from "../../../common/form/CommonCheckBox";
import CloseIcon from "@mui/icons-material/Close";
import SaveIcon from "@mui/icons-material/Save";
import {pagingSalaryTemplate} from "../../SalaryTemplate/SalaryTemplateService";
import KeyIcon from '@mui/icons-material/Key';

function PersonalInformation() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const [uploadedFile, setUploadedFile] = React.useState(false);
    const {staffStore, userStore} = useStore();

    const {
        saveStaff,
        selectedRow,
        getStaffById,
        isProfile,
        getCurrentStaff
    } = staffStore;

    const {handleOpenChangePassword} = userStore;
    const validationSchema = Yup.object({
        firstName: Yup.string()
            .trim()
            .required(t("validation.required")),
        lastName: Yup.string()
            .trim()
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

    useEffect(() => {
        if (isProfile) {
            getCurrentStaff();
        }
    }, [isProfile])

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
                    <Form autoComplete="off" className="min-h-screen flex flex-col justify-between relative">
                        <div className="w-full grid gap-4 grid-cols-12">
                            <div className="col-span-12">
                                <TabAccordion title='Thông tin cá nhân'>
                                    <div className="grid grid-cols-12 gap-2">
                                        <div
                                            className="col-span-12 md:col-span-4  flex flex-col justify-center items-center">
                                            <Avatar className={"!size-36 !text-6xl"}
                                                    src={`${API_ENDPOINT}/${values?.avatar?.filePath}`}
                                            >
                                                {values?.lastName?.charAt(0)?.toUpperCase()}
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
                                            <div className="col-span-12 md:col-span-6 xl:col-span-4">
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
                                            <div className="col-span-12 md:col-span-6 xl:col-span-4">
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
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonTextField
                                                    label={t("Họ và tên")}
                                                    name="displayName"
                                                    disabled
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonSelectInput
                                                    label={t("Giới tính")}
                                                    name="gender"
                                                    options={Gender.getListData()}
                                                    required
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonDateTimePicker
                                                    label={t("Ngày sinh")}
                                                    name="birthDate"
                                                    disableFuture
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonTextField
                                                    label={t("Nơi sinh")}
                                                    name="birthPlace"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonTextField
                                                    label={t("Số điện thoại")}
                                                    name="phoneNumber"
                                                    type="number"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonTextField
                                                    label={t("CCCD")}
                                                    name="idNumber"
                                                    type="number"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonTextField
                                                    label={t("Nơi cấp")}
                                                    name="idNumberIssueBy"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonDateTimePicker
                                                    label={t("Ngày cấp")}
                                                    name="idNumberIssueDate"
                                                    disableFuture
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonTextField
                                                    label={t("Email")}
                                                    name="email"
                                                    required
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-6  xl:col-span-4">
                                                <CommonSelectInput
                                                    label={t("Trình độ học vấn")}
                                                    name="educationLevel"
                                                    options={EducationLevel.getListData()}
                                                />
                                            </div>
                                        </div>
                                        <div className="col-span-12 grid grid-cols-12 gap-2">
                                            <div className="col-span-12 md:col-span-3  xl:col-span-3">
                                                <CommonSelectInput
                                                    label={t("Tình trạng hôn nhân")}
                                                    name="maritalStatus"
                                                    options={MaritalStatus.getListData()}
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Mã số thuế")}
                                                    name="taxCode"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Chiều cao")}
                                                    name="height"
                                                    type="number"
                                                />
                                            </div>
                                            <div className="col-span-12 md:col-span-3  xl:col-span-3">
                                                <CommonTextField
                                                    label={t("Cân nặng")}
                                                    name="weight"
                                                    type="number"
                                                />
                                            </div>
                                        </div>
                                    </div>
                                </TabAccordion>
                            </div>
                            <div className="col-span-12">
                                <TabAccordion title='Hồ sơ nhân viên'>
                                    <div className="col-span-12 grid grid-cols-12 gap-2">
                                        <div className="col-span-12 md:col-span-4  xl:col-span-3">
                                            <CommonTextField
                                                label={t("Mã nhân viên")}
                                                name="staffCode"
                                                disabled={isProfile}
                                                readOnly={isProfile}
                                            />
                                        </div>
                                        <div className="col-span-12 md:col-span-4  xl:col-span-3">
                                            <CommonDateTimePicker
                                                label={t("Ngày tuyển dụng")}
                                                name="recruitmentDate"
                                                disableFuture
                                                disabled={isProfile}
                                                readOnly={isProfile}
                                            />
                                        </div>
                                        <div className="col-span-12 md:col-span-4  xl:col-span-3">
                                            <CommonDateTimePicker
                                                label={t("Ngày bắt đầu chính thức")}
                                                name="startDate"
                                                disableFuture
                                                disabled={isProfile}
                                                readOnly={isProfile}
                                            />
                                        </div>
                                        <div className="col-span-12 md:col-span-4  xl:col-span-3">
                                            <CommonTextField
                                                label={t("Số ngày thử việc")}
                                                name="apprenticeDays"
                                                value={calculateDateDifference(values?.recruitmentDate, values?.startDate)}
                                                disabled={true}
                                                readOnly={isProfile}
                                            />
                                        </div>
                                        <div className="col-span-12 md:col-span-4  xl:col-span-3">
                                            <CommonSelectInput
                                                label={t("Trạng thái nhân viên")}
                                                name="employeeStatus"
                                                options={EmployeeStatus.getListData()}
                                                disabled={isProfile}
                                                readOnly={isProfile}
                                            />
                                        </div>
                                        {/*<div className="col-span-12 md:col-span-4  xl:col-span-3">*/}
                                        {/*    <CommonPagingAutocompleteV2*/}
                                        {/*        label={t("Mẫu hồ sơ nhân viên")}*/}
                                        {/*        name="documentTemplate"*/}
                                        {/*        api={pagingDocumentTemplate}*/}
                                        {/*        disabled={isProfile}*/}
                                        {/*        readOnly={isProfile}*/}
                                        {/*    />*/}
                                        {/*</div>*/}
                                        <div className="col-span-12 md:col-span-4  xl:col-span-3">
                                            <CommonSelectInput
                                                label={t("Loại nhân viên")}
                                                name="staffPhase"
                                                options={StaffPhase.getListData()}
                                                disabled={isProfile}
                                                readOnly={isProfile}
                                            />
                                        </div>
                                        {/*<div className="col-span-12 md:col-span-4 xl:col-span-3">*/}
                                        {/*    <CommonCheckBox*/}
                                        {/*        label={t("Bắt buộc chấm công")}*/}
                                        {/*        name="requireAttendance"*/}
                                        {/*        disabled={isProfile}*/}
                                        {/*        readOnly={isProfile}*/}
                                        {/*    />*/}
                                        {/*</div>*/}
                                        {/*<div className="col-span-12 md:col-span-4 xl:col-span-3">*/}
                                        {/*    <CommonCheckBox*/}
                                        {/*        label={t("Cho phép chấm công ngoài văn phòng")}*/}
                                        {/*        name="allowExternalIpTimekeeping"*/}
                                        {/*        disabled={isProfile}*/}
                                        {/*        readOnly={isProfile}*/}
                                        {/*    />*/}
                                        {/*</div>*/}
                                        <div className="col-span-12 md:col-span-4  xl:col-span-3">
                                            <CommonPagingAutocompleteV2
                                                label={t("Mẫu bảng lương")}
                                                name="salaryTemplate"
                                                api={pagingSalaryTemplate}
                                                disabled={isProfile}
                                                readOnly={isProfile}
                                            />
                                        </div>
                                    </div>
                                </TabAccordion>
                            </div>
                        </div>
                        <div className="w-full sticky bottom-0 bg-white border-t mt-4 py-3 z-10">
                            <div className="flex justify-end w-full">
                                {isProfile ? (
                                    <Button
                                        variant="outlined"
                                        color="secondary"
                                        disabled={isSubmitting}
                                        onClick={handleOpenChangePassword}
                                        className="rounded-lg px-4 py-2 !mr-2 !bg-red-500"
                                        startIcon={<KeyIcon/>}
                                    >
                                        {t("Đổi mật khẩu")}
                                    </Button>

                                ) : (
                                    <Button
                                        variant="outlined"
                                        color="secondary"
                                        disabled={isSubmitting}
                                        onClick={() => {
                                            navigate(`/staff`)
                                        }}
                                        className="rounded-lg px-4 py-2 !mr-2 !bg-red-500"
                                        startIcon={<CloseIcon/>}
                                    >
                                        {t("general.button.close")}
                                    </Button>
                                )}
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
                );
            }}
        </Formik>
    );
}

export default memo(observer(PersonalInformation));
