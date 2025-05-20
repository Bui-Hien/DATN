import "./i18n";
import React, {useCallback, useEffect, useState} from "react";
import {BrowserRouter, Route, Routes, useLocation, useNavigate} from 'react-router-dom';
import AppLayout from "./common/Layout/AppLayout";
import routes from "./RootRoutes";
import {LOGIN_PAGE} from "./appConfig";
import LoginIndex from "./views/Login/LoginIndex";
import {useStore} from "./stores";
import {ToastContainer} from "react-toastify";

function AppWrapper() {
    const navigate = useNavigate();
    const location = useLocation();  // Lấy thông tin đường dẫn hiện tại
    const [isLoginPage, setIsLoginPage] = useState(false);

    const {authStore} = useStore();
    const {
        getCurrentUser,
    } = authStore;

    const initAuth = useCallback(async () => {
        if (location.pathname !== LOGIN_PAGE) {
            const user = await getCurrentUser();
            if (user === null || user === undefined) {
                navigate(LOGIN_PAGE);
            } else {
                setIsLoginPage(true);
            }
        }
    }, [location.pathname, getCurrentUser, navigate]); // Đặt đầy đủ dependency ở đây

    useEffect(() => {
        initAuth();
    }, [initAuth]);

    return (
        <>
            <Routes>
                <Route path={LOGIN_PAGE} element={<LoginIndex/>}/>
            </Routes>
            {isLoginPage && (
                <AppLayout routes={routes}/>
            )}
        </>
    );
}

function App() {
    return (
        <React.StrictMode>
            <BrowserRouter>
                <AppWrapper/>
                <ToastContainer
                    position="top-right"
                    limit={3}
                />
            </BrowserRouter>
        </React.StrictMode>
    );
}

export default App;
