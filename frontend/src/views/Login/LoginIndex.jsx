import * as Yup from "yup";
import {Form, Formik} from "formik";
import {Button, Card, CardContent, Grid, Typography} from "@mui/material";
import {HOME_PAGE} from "../../appConfig";
import {useNavigate} from "react-router-dom";
import CommonTextField from "../../common/form/CommonTextField";
import i18next from "i18next";
import React, {useEffect} from "react";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";

export default observer(function LoginIndex() {
    const {loginStore} = useStore();
    const navigate = useNavigate();

    const {loginObject, resetStore, handleLogin} = loginStore;

    const validationSchema = Yup.object({
        username: Yup.string().required(i18next.t("validation.required")).nullable(),
        password: Yup.string().required(i18next.t("validation.required")).nullable(),
    });

    const handleSubmitForm = async (values) => {
        const success = await handleLogin(values);
        if (success) {
            navigate(HOME_PAGE)
        }
    };
    useEffect(() => {
        return resetStore;
    })

    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <Formik
                validationSchema={validationSchema}
                enableReinitialize
                initialValues={loginObject}
                onSubmit={handleSubmitForm}
            >
                {({isSubmitting, setFieldValue, errors, touched}) => (
                    <Form autoComplete="off">
                        <Card className="w-96 shadow-lg">
                            <CardContent>
                                <Typography variant="h5" className="text-center mb-4 font-bold">
                                    {i18next.t("Đăng nhập")}
                                </Typography>
                                <Grid container spacing={2}>
                                    <Grid item xs={12}>
                                        <CommonTextField
                                            label={i18next.t("Tên đăng nhập")}
                                            fullWidth
                                            name="username"
                                            required
                                        />
                                    </Grid>
                                    <Grid item xs={12}>
                                        <CommonTextField
                                            label={i18next.t("Mật khẩu")}
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
                                            {i18next.t("Đăng nhập")}
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
