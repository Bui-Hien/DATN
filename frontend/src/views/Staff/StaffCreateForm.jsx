import React, {memo, useEffect} from "react";
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
import CommonSelectInput from "../../common/form/CommonSelectInput";
import {Gender} from "../../LocalConstants";
import CommonPagingAutocompleteV2 from "../../common/form/CommonPagingAutocompleteV2";
import {useNavigate} from "react-router-dom";
import {pagingCountry} from "../Country/CountryService";

function StaffCreateForm(props) {
    const navigate = useNavigate();
    const {t} = useTranslation();
    const {staffStore} = useStore();

    const {
        handleClose,
        saveStaff,
        selectedRow,
        openCreateEditPopup,
    } = staffStore;

    const validationSchema = Yup.object({
        firstName: Yup.string()
            .trim()
            .required(t("validation.required")),
        lastName: Yup.string()
            .trim()
            .required(t("validation.required")),
        // displayName: Yup.string()
        //     .trim()
        //     .required(t("validation.required")),
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


    async function handleSaveForm(values) {
        const response = await saveStaff(values);
        navigate(`/staff/${response?.id}`)
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Nhân viên")}
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
                                <div className="grid grid-cols-12 gap-2">
                                    <div className="col-span-12 md:col-span-6">
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
                                    <div className="col-span-12 md:col-span-6">
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
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonTextField
                                            label={t("Tên hiển thị")}
                                            name="displayName"
                                            disabled
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonSelectInput
                                            label={t("Giới tính")}
                                            name="gender"
                                            options={Gender.getListData()}
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonTextField
                                            label={t("Email")}
                                            name="email"
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonPagingAutocompleteV2
                                            label={t("Quốc tịch")}
                                            name="nationality"
                                            api={pagingCountry}
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

export default memo(observer(StaffCreateForm));
