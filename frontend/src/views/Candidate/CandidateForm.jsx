import React, {memo, useEffect} from "react";
import {observer} from "mobx-react-lite";
import {Button} from "@mui/material";
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {useNavigate, useParams} from "react-router-dom";
import SaveIcon from "@mui/icons-material/Save";
import CloseIcon from "@mui/icons-material/Close";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import CommonTextField from "../../common/form/CommonTextField";
import TabAccordion from "../../common/Accordion/TabAccordion";
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {EducationLevel, Gender, MaritalStatus} from "../../LocalConstants";
import CommonDateTimePicker from "../../common/form/CommonDateTimePicker";
import CommonNumberInput from "../../common/form/CommonNumberInput";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {pagingStaff} from "../Staff/StaffService";
import {pagingRecruitmentRequest} from "../RecruitmentRequest/RecruitmentRequestService";
import {pagingPosition} from "../Position/PositionService";

function CandidateForm() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {t} = useTranslation();
    const {candidateStore} = useStore();
    const {
        saveCandidate,
        selectedRow,
        handleOpenCreateEdit,
    } = candidateStore;

    const validationSchema = Yup.object({
        firstName: Yup.string().trim().nullable().required(t("validation.required")),
        lastName: Yup.string().trim().nullable().required(t("validation.required")),
        gender: Yup.number().nullable().required(t("validation.required")),
        email: Yup.string().trim().nullable().email(t("validation.invalid_email")).required(t("validation.required")),
        workExperience: Yup.string().trim().nullable().required(t("validation.required")),
        recruitmentRequest: Yup.object().nullable().required(t("validation.required")),
    });

    useEffect(() => {
        handleOpenCreateEdit(id);
    }, [id]);

    const handleSaveForm = async (values) => {
        const success = await saveCandidate(values);
        console.log(success);
        if (success?.status < 400) navigate("/candidate");
    };

    return (
        <Formik
            initialValues={selectedRow}
            validationSchema={validationSchema}
            enableReinitialize
            onSubmit={handleSaveForm}
        >
            {({isSubmitting, values, setFieldValue}) => (
                <Form autoComplete="off" className="min-h-screen flex flex-col justify-between pt-5">
                    <div className="w-full grid gap-4 grid-cols-12">
                        <div className="col-span-12">
                            <TabAccordion title="Thông tin cá nhân">
                                <div className="grid grid-cols-12 gap-4">
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
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
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Tên")}
                                            name="lastName"
                                            required
                                            onChange={(e) => {
                                                const lastName = e.target.value;
                                                setFieldValue("lastName", lastName);
                                                setFieldValue("displayName", `${values.firstName || ""} ${lastName.trim()}`.trim());
                                            }}
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Họ và tên")}
                                            name="displayName"
                                            disabled
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonSelectInput
                                            label={t("Giới tính")}
                                            name="gender"
                                            options={Gender.getListData()}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonDateTimePicker
                                            label={t("Ngày sinh")}
                                            name="birthDate"
                                            disableFuture
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Nơi sinh")}
                                            name="birthPlace"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Số điện thoại")}
                                            name="phoneNumber"
                                            type="number"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("CCCD")}
                                            name="idNumber"
                                            type="number"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Nơi cấp")}
                                            name="idNumberIssueBy"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonDateTimePicker
                                            label={t("Ngày cấp")}
                                            name="idNumberIssueDate"
                                            disableFuture
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Email")}
                                            name="email"
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonSelectInput
                                            label={t("Trình độ học vấn")}
                                            name="educationLevel"
                                            options={EducationLevel.getListData()}
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonSelectInput
                                            label={t("Tình trạng hôn nhân")}
                                            name="maritalStatus"
                                            options={MaritalStatus.getListData()}
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Chiều cao")}
                                            name="height"
                                            type="number"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Cân nặng")}
                                            name="weight"
                                            type="number"
                                        />
                                    </div>
                                </div>
                            </TabAccordion>
                        </div>

                        <div className="col-span-12">
                            <TabAccordion title="Thông tin tuyển dụng">
                                <div className="grid grid-cols-12 gap-2">
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonTextField
                                            label={t("Mã ứng viên")}
                                            name="candidateCode"
                                            readOnly
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonDateTimePicker
                                            label={t("Ngày phỏng vấn")}
                                            name="interviewDate"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonNumberInput
                                            label={t("Mức lương mong muốn")}
                                            name="desiredPay"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonDateTimePicker
                                            label={t("Ngày có thể làm việc")}
                                            name="possibleWorkingDate"
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonPagingAutocompleteV2
                                            label={t("Nhân viên giới thiệu")}
                                            name="introducer"
                                            api={pagingStaff}
                                            getOptionLabel={(option) =>
                                                option?.staffCode && option?.displayName
                                                    ? `${option.staffCode} - ${option.displayName}`
                                                    : option?.staffCode || option?.displayName || ""
                                            }
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonPagingAutocompleteV2
                                            label={t("Yêu cầu tuyển dụng")}
                                            name="recruitmentRequest"
                                            api={pagingRecruitmentRequest}
                                            handleChange={(_, value) => {
                                                setFieldValue("recruitmentRequest", value);
                                                setFieldValue("position", value?.position);
                                            }}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-3 xl:col-span-4">
                                        <CommonPagingAutocompleteV2
                                            label={t("Vị trí ứng tuyển")}
                                            name="position"
                                            api={pagingPosition}
                                            readOnly
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Kinh nghiệm làm việc"
                                            name="workExperience"
                                            multiline
                                            rows={5}
                                            required
                                        />
                                    </div>
                                </div>
                            </TabAccordion>
                        </div>
                    </div>

                    <div className="w-full sticky bottom-0 bg-white border-t mt-4 py-3 z-10 flex justify-end">
                        <Button
                            variant="outlined"
                            color="secondary"
                            disabled={isSubmitting}
                            onClick={() => navigate("/candidate")}
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
                </Form>
            )}
        </Formik>
    );
}

export default memo(observer(CandidateForm));