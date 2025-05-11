import React from 'react';
import styles from "./styles.module.css";
import {Icon, Tooltip} from "@mui/material";

const ErrorIcon = ({helperText}) => {
    return (
        <Tooltip arrow title={helperText}>
            <Icon color="error" className={styles.icon}/>
        </Tooltip>
    )
}

export default React.memo(ErrorIcon);