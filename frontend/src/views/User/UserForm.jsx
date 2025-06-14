import React, {memo, useEffect, useState} from "react";
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
import {pagingStaff} from "../Staff/StaffService";
import {pagingRole} from "./UserService";
import RestartAltIcon from '@mui/icons-material/RestartAlt';

function UserForm() {
    const {t} = useTranslation();
    const {userStore} = useStore();

    const {
        handleClose,
        saveUser,
        selectedRow,
        openCreateEditPopup,
    } = userStore;
    const [updatePassWord, setUpdatePassWord] = useState(false);

    const validationSchema = (updatePassword) =>
        Yup.object({
            person: Yup.object().required(t("validation.required")),
            username: Yup.string().required(t("validation.required")),

            roles: Yup.array()
                .of(Yup.object())
                .min(1, t("validation.required")),

            password: updatePassword
                ? Yup.string().required(t("validation.required"))
                : Yup.string().notRequired(),

            confirmPassword: updatePassword
                ? Yup.string()
                    .required(t("validation.required"))
                    .oneOf([Yup.ref("password")], t("validation.confirmPasswordNotMatch"))
                : Yup.string().notRequired(),
        });

    async function handleSaveForm(values) {
        await saveUser(values);
    }

    useEffect(() => {
        setUpdatePassWord(!selectedRow?.id)
    }, [])

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Người dùng")}
            onClosePopup={handleClose}
            isCreate={!selectedRow?.id}
        >
            <Formik
                validationSchema={validationSchema(updatePassWord)}
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
                                        <CommonPagingAutocompleteV2
                                            label="Nhân viên"
                                            name="person"
                                            handleChange={(_, value) => {
                                                setFieldValue("person", value);
                                                setFieldValue("username", value?.staffCode);
                                            }}
                                            api={pagingStaff}
                                            getOptionLabel={(option) =>
                                                option?.staffCode && option?.displayName
                                                    ? `${option.staffCode} - ${option.displayName}`
                                                    : option?.staffCode || option?.displayName || ""
                                            }
                                            required
                                        />
                                    </div>
                                    <div className="col-span-12 md:col-span-6">
                                        <CommonTextField
                                            label="Tên đăng nhập"
                                            name="username"
                                            required
                                        />
                                    </div>
                                    {updatePassWord && (
                                        <div className="col-span-12 md:col-span-6">
                                            <CommonTextField
                                                label="Mật khẩu"
                                                name="password"
                                                required/>
                                        </div>
                                    )}
                                    {updatePassWord && (
                                        <div className="col-span-12 md:col-span-6">
                                            <CommonTextField
                                                label="Mật khẩu xác nhận"
                                                name="confirmPassword"
                                                required/>
                                        </div>
                                    )}
                                    <div className="col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label="Quyền hạn người dùng"
                                            name="roles"
                                            api={pagingRole}
                                            required
                                            multiple
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
                                        variant="outlined"
                                        color="secondary"
                                        disabled={isSubmitting}
                                        onClick={() => {
                                            setUpdatePassWord(!updatePassWord)
                                        }}
                                        className="rounded-lg px-4 py-2 !mr-2 !bg-green-400-500"
                                        startIcon={<RestartAltIcon/>}
                                    >
                                        {t("Đặt lại mật khẩu")}
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

export default memo(observer(UserForm));
