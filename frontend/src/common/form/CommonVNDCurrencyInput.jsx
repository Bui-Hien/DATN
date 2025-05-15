import React, {memo, useEffect, useRef, useState} from "react";
import {TextField} from "@mui/material";
import {FastField, getIn} from "formik";
import NumberFormat from "react-number-format";
import PropTypes from "prop-types";

const CommonVNDCurrencyInput = (props) => (
    <FastField {...props} name={props.name} shouldUpdate={areEqual}>
        {({ field, meta, form }) => (
            <Component {...props} field={field} meta={meta} setFieldValue={form.setFieldValue} formik={form} />
        )}
    </FastField>
);

const NumericFormatCustom = React.forwardRef(({ onChange, value, ...other }, ref) => (
    <NumberFormat
        {...other}
        getInputRef={ref}
        value={value}
        onValueChange={(values) => {
            onChange({
                target: {
                    name: other.name,
                    value: values.value,
                },
            });
        }}
        thousandSeparator
        valueIsNumericString
    />
));

NumericFormatCustom.propTypes = {
    name: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
};

const Component = ({
                       name,
                       label,
                       type = "text",
                       debounceTime = 400,
                       notDelay,
                       field,
                       meta,
                       disabled,
                       placeholder,
                       minRowArea,
                       required,
                       className = "",
                       onChange,
                       setFieldValue,
                       oldStyle,
                       readOnly,
                       variant,
                       suffix = "",
                       textAlignRight,
                       formik,
                   }) => {
    const [value, setValue] = useState(field.value ?? "");
    const timer = useRef();

    useEffect(() => {
        if (field.value !== value) setValue(field.value ?? "");
    }, [field.value]);

    useEffect(() => {
        return () => {
            if (timer.current) clearTimeout(timer.current);
        };
    }, []);

    const handleChange = (e) => {
        const val = e.target.value;
        setValue(val);
        if (!notDelay) {
            if (timer.current) clearTimeout(timer.current);
            if (onChange) {
                timer.current = setTimeout(() => onChange(e), debounceTime);
            } else {
                timer.current = setTimeout(() => setFieldValue(name, val ? val : null), debounceTime);
            }
        } else {
            if (onChange) {
                onChange(e);
            } else {
                setFieldValue(name, val ? val : null);
            }
        }
    };

    return (
        <>
            {label && (
                <label htmlFor={name} className={`${oldStyle ? "old-label" : "label-container"}`}>
                    {label} {required && <span style={{ color: "red" }}> * </span>}
                </label>
            )}

            <TextField
                variant={variant || "outlined"}
                id={name}
                name={name}
                value={value}
                fullWidth
                onChange={readOnly ? undefined : handleChange}
                placeholder={placeholder}
                disabled={disabled || readOnly}
                type={type}
                error={Boolean(meta?.touched && meta?.error)}
                helperText={meta?.touched && meta?.error ? meta.error : ""}
                InputProps={{
                    readOnly,
                    inputComponent: NumericFormatCustom,
                    style: textAlignRight ? { textAlign: "right" } : {},
                    endAdornment: suffix && <span style={{ marginRight: "8px", color: "#757575" }}>{suffix}</span>,
                }}
                InputLabelProps={{
                    htmlFor: name,
                    shrink: true,
                }}
                minRows={minRowArea}
                className={`${oldStyle ? "" : "input-container"} ${readOnly ? "read-only" : ""} ${className}`}
            />
        </>
    );
};

const areEqual = (prevProps, nextProps) =>
    prevProps.readOnly !== nextProps.readOnly ||
    prevProps.value !== nextProps.value ||
    prevProps.onChange !== nextProps.onChange ||
    prevProps.disabled !== nextProps.disabled ||
    prevProps.name !== nextProps.name ||
    Object.keys(prevProps).length !== Object.keys(nextProps).length ||
    getIn(prevProps.formik?.values, prevProps.name) !== getIn(nextProps.formik?.values, prevProps.name) ||
    getIn(prevProps.formik?.errors, prevProps.name) !== getIn(nextProps.formik?.errors, prevProps.name) ||
    getIn(prevProps.formik?.touched, prevProps.name) !== getIn(nextProps.formik?.touched, prevProps.name);

export default memo(CommonVNDCurrencyInput, areEqual);
