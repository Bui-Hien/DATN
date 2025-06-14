import React, {memo, useEffect, useState} from "react";
import {FormControl, styled, TextField} from "@mui/material";
import {FastField, getIn} from "formik";

// Dùng styled API thay thế makeStyles
const EndAdornmentWrapper = styled("span")({
    position: "relative",
    left: 235,
    bottom: 29,
});

const MultilineStyle = {
    "& .MuiInputBase-root": {
        height: "unset !important",
        padding: "10px !important",
    },
};

const CommonTextField = (props, ref) => {
    return (
        <FastField
            {...props}
            name={props.name}
            shouldUpdate={shouldComponentUpdate}
        >
            {({field, meta}) => {
                return <MyTextField {...props} field={field} meta={meta} ref={ref}/>;
            }}
        </FastField>
    );
};

const shouldComponentUpdate = (nextProps, currentProps) => {
    return (
        nextProps.name !== currentProps.name ||
        nextProps.value !== currentProps.value ||
        nextProps.onChange !== currentProps.onChange ||
        nextProps.label !== currentProps.label ||
        nextProps.required !== currentProps.required ||
        nextProps.disabled !== currentProps.disabled ||
        nextProps.readOnly !== currentProps.readOnly ||
        nextProps.formik?.isSubmitting !== currentProps.formik?.isSubmitting ||
        Object.keys(nextProps).length !== Object.keys(currentProps).length ||
        getIn(nextProps.formik.values, currentProps.name) !==
        getIn(currentProps.formik.values, currentProps.name) ||
        getIn(nextProps.formik.errors, currentProps.name) !==
        getIn(currentProps.formik.errors, currentProps.name) ||
        getIn(nextProps.formik.touched, currentProps.name) !==
        getIn(currentProps.formik.touched, currentProps.name)
    );
};

const MyTextField = ({
                         label,
                         name,
                         variant,
                         size,
                         type,
                         endAdornment,
                         validate,
                         multiline,
                         timeOut = 500,
                         required = false,
                         oldStyle = false,
                         readOnly = false,
                         field,
                         meta,
                         ...otherProps
                     }) => {
    const [value, setValue] = useState(field.value ?? "");
    const [t, setT] = useState(undefined);

    const onChange = (e) => {
        if (readOnly) return;
        e.persist();
        setValue(e.target.value);

        if (!otherProps.notDelay) {
            if (t) clearTimeout(t);
            setT(
                setTimeout(() => {
                    if (otherProps.onChange) {
                        otherProps.onChange(e);
                    } else {
                        field.onChange(e);
                    }
                }, timeOut)
            );
        } else {
            if (otherProps.onChange) {
                otherProps.onChange(e);
            } else {
                field.onChange(e);
            }
        }
    };

    useEffect(() => {
        if (field.value !== undefined) {
            setValue(field.value ?? "");
        }
    }, [field.value]);

    useEffect(() => {
        if (otherProps.value !== undefined) {
            setValue(otherProps.value ?? "");
        }
    }, [otherProps.value]);

    const configTextfield = {
        ...field,
        ...otherProps,
        multiline: multiline,
        value: value,
        id: name,
        onChange: onChange,
        fullWidth: true,
        variant: variant || "outlined",
        size: size || "small",
        type: type || "",
        InputLabelProps: {
            htmlFor: name,
            shrink: true,
        },
        sx: {
            ...(multiline ? MultilineStyle : {}),
            ...(readOnly ? {pointerEvents: "none", backgroundColor: "#f5f5f5"} : {}),
        },
    };

    if (meta && meta.touched && meta.error) {
        configTextfield.error = true;
        configTextfield.helperText = meta.error;
    }

    return (
        <>
            {label && (
                <label
                    htmlFor={name}
                    className={`${oldStyle ? "old-label" : "label-container"}`}
                >
                    {label} {(validate || required) && <span style={{color: "red"}}> * </span>}
                </label>
            )}

            <FormControl fullWidth>
                <TextField {...configTextfield} />
                {endAdornment && <EndAdornmentWrapper>{endAdornment}</EndAdornmentWrapper>}
            </FormControl>
        </>
    );
};

export default memo(CommonTextField);
