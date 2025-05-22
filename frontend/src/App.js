import "./i18n";
import React, {Suspense, useCallback, useEffect, useState} from "react";
import {BrowserRouter, Route, Routes, useLocation, useNavigate} from 'react-router-dom';
import AppLayout from "./common/Layout/AppLayout";
import routes from "./RootRoutes";
import {LOGIN_PAGE} from "./appConfig";
import LoginIndex from "./views/Login/LoginIndex";
import {useStore} from "./stores";
import {ToastContainer} from "react-toastify";
import Loading from "./common/Layout/Loading";

function AppWrapper() {
    const navigate = useNavigate();
    const location = useLocation();
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true); // Thêm loading state

    const { authStore } = useStore();
    const { getCurrentUser } = authStore;

    const initAuth = useCallback(async () => {
        try {
            const user = await getCurrentUser();
            if (!user && location.pathname !== LOGIN_PAGE) {
                navigate(LOGIN_PAGE);
                setIsAuthenticated(false);
            } else if (user) {
                setIsAuthenticated(true);
            }
        } catch (error) {
            console.error('Auth error:', error);
            if (location.pathname !== LOGIN_PAGE) {
                navigate(LOGIN_PAGE);
            }
            setIsAuthenticated(false);
        } finally {
            setIsLoading(false);
        }
    }, [getCurrentUser, navigate, location.pathname]);

    useEffect(() => {
        initAuth();
    }, [initAuth]);

    // Hiển thị loading khi đang xác thực
    if (isLoading) {
        return <Loading />;
    }

    // Nếu chưa authenticated và không phải login page
    if (!isAuthenticated && location.pathname !== LOGIN_PAGE) {
        return <Loading />;
    }

    return (
        <Suspense fallback={<Loading />}>
            <Routes>
                <Route path={LOGIN_PAGE} element={<LoginIndex />} />
                <Route path="/*" element={<AppLayout routes={routes} />} />
            </Routes>
        </Suspense>
    );
}

function App() {
    return (
        <BrowserRouter>
            <AppWrapper/>
            <ToastContainer
                position="top-right"
                limit={3}
            />
        </BrowserRouter>
    );
}

export default App;