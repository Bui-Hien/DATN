import React, {memo, useEffect, useState} from "react";
import {Form, Formik} from "formik";
import {useTranslation} from "react-i18next";
import {useStore} from "../../stores";
import * as Yup from "yup";
import CommonPopupV2 from "../../common/CommonPopupV2";
import {Button, DialogActions, DialogContent, Grid} from "@mui/material";
import CommonTextField from "../../common/form/CommonTextField";
import {observer} from "mobx-react-lite";

function CountryForm(props) {
    const {t} = useTranslation();
    const {countryStore} = useStore();

    const {
        handleClose,
        saveCountry,
        selectCountry,
        openCreateEditPopup,
    } = countryStore;

    const validationSchema = Yup.object({});

    async function handleSaveForm(values) {
        await saveCountry(values);
    }

    return (
        <CommonPopupV2
            size="sm"
            scroll={"body"}
            open={openCreateEditPopup}
            noDialogContent
            title={(selectCountry?.id ? t("general.button.edit") : t("general.button.add")) + ' ' + t("navigation.hrIntroduceCost.title")}
            onClosePopup={handleClose}
        >
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={selectCountry}
                onSubmit={handleSaveForm}
            >
                {({isSubmitting, values, setFieldValue, initialValues}) => {
                    return (
                        <Form autoComplete="off">
                            <DialogContent className="dialog-body p-12">
                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <CommonTextField
                                            label="Ghi chú"
                                            name="code"
                                        />
                                    </Grid>
                                    <Grid item xs={12}>
                                        <CommonTextField
                                            label="Ghi chú"
                                            name="name"
                                        />
                                    </Grid>
                                    <Grid item xs={12}>
                                        <CommonTextField
                                            label="Ghi chú"
                                            name="description"
                                        />
                                    </Grid>
                                </Grid>
                            </DialogContent>

                            <DialogActions className="dialog-footer px-12">
                                <div className="flex flex-space-between flex-middle">
                                    <Button
                                        variant="contained"
                                        className="mr-12 btn btn-secondary d-inline-flex"
                                        color="secondary"
                                        disabled={isSubmitting}
                                        onClick={handleClose}
                                    >
                                        {t("general.button.close")}
                                    </Button>
                                    <Button
                                        className="mr-0 btn btn-primary d-inline-flex"
                                        variant="contained"
                                        color="primary"
                                        type="submit"
                                        disabled={isSubmitting}
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

export default memo(observer(CountryForm));
