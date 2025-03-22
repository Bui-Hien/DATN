import { observer } from "mobx-react";
import * as Yup from "yup";
import { Form, Formik } from "formik";
import { useRouter } from "next/navigation";
import { Validation } from "@/app/LocalConstants";
import { useStore } from "@/app/stores";
import { TextField, Button, Card, CardContent, Typography } from "@mui/material";

export default observer(function Login() {
    const router = useRouter();
    const { loginStore } = useStore();
    const { intactLoginObject, handleLogin, handleSetLoginObject } = loginStore;

    const validationSchema = Yup.object({
        username: Yup.string().required(Validation.REQUIRED).nullable(),
        password: Yup.string().required(Validation.REQUIRED).nullable(),
    });

    const handleSubmitForm = async (values) => {
        handleSetLoginObject(values);
        const success = await handleLogin();
        if (success) {
            router.push("/");
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
                {({ isSubmitting, setFieldValue, errors, touched }) => (
                    <Form autoComplete="off">
                        <Card className="w-96 shadow-lg">
                            <CardContent>
                                <Typography variant="h5" className="text-center mb-4 font-bold">
                                    Đăng Nhập
                                </Typography>

                                <TextField
                                    label="Username"
                                    fullWidth
                                    name="username"
                                    variant="outlined"
                                    margin="normal"
                                    onChange={(e) => setFieldValue("username", e.target.value)}
                                    error={touched.username && Boolean(errors.username)}
                                    helperText={touched.username && errors.username}
                                />

                                <TextField
                                    label="Mật khẩu"
                                    type="password"
                                    fullWidth
                                    name="password"
                                    variant="outlined"
                                    margin="normal"
                                    onChange={(e) => setFieldValue("password", e.target.value)}
                                    error={touched.password && Boolean(errors.password)}
                                    helperText={touched.password && errors.password}
                                />

                                <Button type="submit" variant="contained" color="primary" fullWidth disabled={isSubmitting}>
                                    Đăng nhập
                                </Button>
                            </CardContent>
                        </Card>
                    </Form>
                )}
            </Formik>
        </div>
    );
})
