import React, { memo } from "react";
import { FastField, getIn } from "formik";
import "../SearchBox.scss";
import {FormControl} from "@mui/material";
import SearchIcon from '@mui/icons-material/Search';
const CommonInputSearch = (props) => (
    <FastField {...props} name={props.name} shouldUpdate={shouldComponentUpdate}>
        {({ field, form }) => <Component {...props} field={field} form={form} />}
    </FastField>
);

function Component({ name, hideSubmitIcon, field, form, disabled, placeholder, className }) {
    return (
        <FormControl fullWidth>
            <div className={`search-box ${className || ""}`}>
                <input
                    {...field}
                    id={name}
                    disabled={disabled}
                    placeholder={placeholder}
                    className="search-input"
                    autoComplete="off"
                />
                {!hideSubmitIcon && (
                    <button type="submit" className="btn-search flex items-center justify-center">
                        <SearchIcon />
                    </button>
                )}
            </div>
        </FormControl>
    );
}

const shouldComponentUpdate = (nextProps, currentProps) => {
    return (
        nextProps.name !== currentProps.name ||
        nextProps.value !== currentProps.value ||
        nextProps.disabled !== currentProps.disabled ||
        nextProps.hideSubmitIcon !== currentProps.hideSubmitIcon ||
        // So sánh giá trị trong formik
        getIn(nextProps.formik?.values || {}, currentProps.name) !== getIn(currentProps.formik?.values || {}, currentProps.name) ||
        getIn(nextProps.formik?.errors || {}, currentProps.name) !== getIn(currentProps.formik?.errors || {}, currentProps.name) ||
        getIn(nextProps.formik?.touched || {}, currentProps.name) !== getIn(currentProps.formik?.touched || {}, currentProps.name)
    );
};

export default memo(CommonInputSearch);
