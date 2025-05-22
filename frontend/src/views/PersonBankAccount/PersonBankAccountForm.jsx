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
import {pagingBank} from "../Bank/BankService";
import CommonCheckBox from "../../common/form/CommonCheckBox";
import CommonNumberInput from "../../common/form/CommonNumberInput";

function PersonBankAccountForm(props) {
    const {t} = useTranslation();
    const {personBankAccountStore, staffStore} = useStore();

    const {
        handleClose,
        savePersonBankAccount,
        selectedRow,
        openCreateEditPopup,
    } = personBankAccountStore;


    const validationSchema = Yup.object({
        bank: Yup.object().nullable().required(t("validation.required")),
        bankAccountName: Yup.string().nullable().required(t("validation.required")),
        bankAccountNumber: Yup.string().nullable().required(t("validation.required")),
        bankBranch: Yup.string().nullable(),
        isMain: Yup.boolean().nullable(),
    });


    async function handleSaveForm(values) {
        if (!staffStore.selectedRow?.id) return
        const newValues = {
            ...values,
            person: staffStore.selectedRow
        }
        await savePersonBankAccount(newValues);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectedRow?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("Tài khoản cá nhân")}
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
                                            label="Tên tài khoản"
                                            name="bankAccountName"
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonNumberInput
                                            label={t("Số tài khoản")}
                                            name="bankAccountNumber"
                                            required/>
                                    </div>
                                    <div className="col-span-12">
                                        <CommonPagingAutocompleteV2
                                            label={t("Ngân hàng")}
                                            name="bank"
                                            api={pagingBank}
                                        />
                                    </div>
                                    <div className="col-span-12">
                                        <CommonTextField
                                            label="Chi nhánh"
                                            name="bankBranch"
                                        />
                                    </div>

                                    <div className="col-span-12">
                                        <CommonCheckBox
                                            label="Là tài khoản chính"
                                            name="isMain"
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

export default memo(observer(PersonBankAccountForm));
