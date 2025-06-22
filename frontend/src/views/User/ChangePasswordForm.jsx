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
import {UserObject} from "./User";

function ChangePasswordForm() {
    const {t} = useTranslation();
    const {userStore} = useStore();

    const {
        handleClose,
        handleChangePassword,
        openChangePassword,
    } = userStore;

    const validationSchema = () =>
        Yup.object({
            oldPassword: Yup.string().required(t("validation.required")),
            password: Yup.string().required(t("validation.required")),
            confirmPassword:
                Yup.string()
                    .required(t("validation.required"))
                    .oneOf([Yup.ref("password")], t("Mật khẩu mới và mật khẩu xác nhận không khớp"))
        });

    async function handleSaveForm(values) {
        await handleChangePassword(values);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openChangePassword}
            noDialogContent
            title={"Đổi mật khẩu"}
            onClosePopup={handleClose}
            isCreate={false}
        >
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={JSON.parse(JSON.stringify(new UserObject()))}
                onSubmit={handleSaveForm}
            >
                {({isSubmitting, values, setFieldValue, initialValues}) => {
                    return (
                        <Form autoComplete="off">
                            <DialogContent className="p-6">
                                <div className={"grid grid-cols-12 gap-2"}>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mật khẩu hiện tại"
                                            name="oldPassword"
                                            type={"password"}
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mật khẩu mới"
                                            name="password"
                                            type={"password"}
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Mật khẩu xác nhận"
                                            name="confirmPassword"
                                            type={"password"}
                                            required/>
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

export default memo(observer(ChangePasswordForm));
