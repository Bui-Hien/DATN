import React from "react";
import { CircularProgress } from "@mui/material";

const Loading = () => {
    return (
        <div className="flex items-center justify-center min-h-screen w-full">
            <CircularProgress />
        </div>
    );
};

export default Loading;
