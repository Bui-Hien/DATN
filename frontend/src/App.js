import "./i18n";
import React from "react";
import {BrowserRouter} from 'react-router-dom';
import MiniDrawer from "./views/Layout/AppBar";

function App() {

    return (
        <React.StrictMode>
            <BrowserRouter>
            <MiniDrawer/>
            </BrowserRouter>
        </React.StrictMode>
    );
}

export default App;
