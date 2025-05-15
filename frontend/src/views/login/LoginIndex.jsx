import * as Yup from "yup";
import {Form, Formik} from "formik";
import {Button, Card, CardContent, Grid, Typography} from "@mui/material";
import {useStore} from "../../stores";
import {HOME_PAGE} from "../../appConfig";
import {useNavigate} from "react-router-dom";
import CommonTextField from "../../common/form/CommonTextField";
import i18next from "i18next";
import React from "react";
import {observer} from "mobx-react-lite";

export default observer(function LoginIndex() {
    const {loginStore} = useStore();
    const {intactLoginObject, handleLogin} = loginStore;
    const navigate = useNavigate();

    const validationSchema = Yup.object({
        username: Yup.string().required(i18next.t("validate.required")).nullable(),
        password: Yup.string().required(i18next.t("validate.required")).nullable(),
    });

    const handleSubmitForm = async (values) => {
        const success = await handleLogin(values);
        if (success) {
            navigate(HOME_PAGE)
        }
    };

    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={intactLoginObject}
                onSubmit={handleSubmitForm}
            >
                {({isSubmitting, setFieldValue, errors, touched}) => (
                    <Form autoComplete="off">
                        <Card className="w-96 shadow-lg">
                            <CardContent>
                                <Typography variant="h5" className="text-center mb-4 font-bold">
                                    {i18next.t("login.title")}
                                </Typography>
                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <CommonTextField
                                            label={i18next.t("login.userName")}
                                            fullWidth
                                            name="username"
                                            required
                                        />
                                    </Grid>
                                    <Grid item xs={12}>
                                        <CommonTextField
                                            label={i18next.t("login.password")}
                                            type="password"
                                            fullWidth
                                            name="password"
                                            required
                                        />
                                    </Grid>
                                    <Grid item xs={12}>
                                        <Button type="submit" variant="contained" color="primary" fullWidth
                                                disabled={isSubmitting}
                                        >
                                            {i18next.t("login.submit")}
                                        </Button>
                                    </Grid>
                                </Grid>
                            </CardContent>
                        </Card>
                    </Form>
                )}
            </Formik>
        </div>
    );
})
