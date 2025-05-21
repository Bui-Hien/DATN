import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";
import {FastField, getIn} from "formik";
import React from "react";
import {makeStyles} from "@mui/styles";

const useStyles = makeStyles(() => ({
    container: {
        "& .MuiAutocomplete-inputRoot": {
            paddingTop: "0px !important",
            paddingBottom: "0px !important",
        },
    },
    autoHeight: {
        "& > div": {
            height: "auto !important",
        },
    },
}));

const CommonSelectInputV2 = (props) => {
    return (
        <FastField {...props} name={props.name} shouldUpdate={shouldComponentUpdate}>
            {({field, meta, form}) => (
                <MySelectInput
                    {...props}
                    field={field}
                    meta={meta}
                    setFieldValue={form.setFieldValue}
                />
            )}
        </FastField>
    );
};

function MySelectInput({
                           name,
                           label,
                           field,
                           meta,
                           setFieldValue,
                           options = [],
                           handleChange,
                           required,
                           getOptionDisabled,
                           getOptionLabel,
                           readOnly = false,
                           oldStyle = false,
                           ...otherProps
                       }) {
    const classes = useStyles();

    const defaultHandleChange = (_, value) => {
        if (readOnly) return;
        setFieldValue(name, value || null);
    };

    const defaultGetOptionLabel = (option) =>
        option?.[otherProps?.displayName || "name"] || "";

    return (
        <Autocomplete
            {...field}
            {...otherProps}
            id={name}
            options={options}
            disableClearable={readOnly}
            className={`${oldStyle ? "" : "input-container"} ${classes.container} ${readOnly ? "read-only" : ""}`}
            onChange={handleChange || defaultHandleChange}
            getOptionLabel={getOptionLabel || defaultGetOptionLabel}
            getOptionSelected={(option, value) => option?.id === value?.id}
            getOptionDisabled={readOnly ? () => true : getOptionDisabled}
            readOnly={readOnly}
            openOnFocus={!readOnly}
            onKeyDown={(event) => {
                if (event.key === "Enter") {
                    event.stopPropagation();
                    event.preventDefault();
                }
            }}
            renderInput={(params) => (
                <>
                    {label && (
                        <label htmlFor={name} className={`${oldStyle ? "old-label" : "label-container"}`}>
                            {label}
                            {required && <span style={{color: "red"}}> * </span>}
                        </label>
                    )}
                    <TextField
                        {...params}
                        variant={otherProps?.variant || "outlined"}
                        inputProps={{
                            ...params.inputProps,
                            autoComplete: "off",
                            readOnly: readOnly,
                            style: readOnly
                                ? {
                                    ...params.inputProps.style,
                                    color: "rgba(0, 0, 0, 0.87)",
                                    cursor: "text",
                                    opacity: 1,
                                }
                                : params.inputProps.style,
                        }}
                        InputProps={{
                            ...params.InputProps,
                            readOnly: readOnly,
                            style: readOnly
                                ? {
                                    ...params.InputProps.style,
                                    color: "rgba(0, 0, 0, 0.87)",
                                    backgroundColor: "rgba(0, 0, 0, 0.02)",
                                    opacity: 1,
                                }
                                : params.InputProps.style,
                        }}
                        className={`${classes.autoHeight} ${readOnly ? "read-only" : ""}`}
                        error={meta && meta.touched && meta.error}
                        helperText={meta && meta.touched && meta.error ? meta.error : ""}
                        required={required}
                    />
                </>
            )}
        />
    );
}

const shouldComponentUpdate = (nextProps, currentProps) => {
    return (
        nextProps.name !== currentProps.name ||
        nextProps.value !== currentProps.value ||
        nextProps.handleChange !== currentProps.handleChange ||
        nextProps.label !== currentProps.label ||
        nextProps.required !== currentProps.required ||
        nextProps.options !== currentProps.options ||
        nextProps.disabled !== currentProps.disabled ||
        nextProps.readOnly !== currentProps.readOnly ||
        nextProps.getOptionDisabled !== currentProps.getOptionDisabled ||
        nextProps.formik.isSubmitting !== currentProps.formik.isSubmitting ||
        Object.keys(nextProps).length !== Object.keys(currentProps).length ||
        getIn(nextProps.formik.values, currentProps.name) !== getIn(currentProps.formik.values, currentProps.name) ||
        getIn(nextProps.formik.errors, currentProps.name) !== getIn(currentProps.formik.errors, currentProps.name) ||
        getIn(nextProps.formik.touched, currentProps.name) !== getIn(currentProps.formik.touched, currentProps.name)
    );
};

export default React.memo(CommonSelectInputV2);
