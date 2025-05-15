import React, { useEffect, useState } from "react";
import { FastField, getIn } from "formik";
import { TextField } from "@mui/material";
import * as i18n from "i18next";

const CommonDateTime = (props) => {
  return (
      <FastField
          {...props}
          name={props.name}
          shouldUpdate={shouldComponentUpdate}
      >
        {({ field, meta, form }) => {
          return (
              <MyDateTimePicker
                  {...props}
                  field={field}
                  meta={meta}
                  setFieldValue={form.setFieldValue}
              />
          );
        }}
      </FastField>
  );
};

const MyDateTimePicker = ({
                            disablePast,
                            disableFuture,
                            name,
                            size,
                            variant,
                            defaultValue,
                            isDateTimePicker,
                            notDelay,
                            field,
                            meta,
                            setFieldValue,
                            readOnly = false,
                            ...otherProps
                          }) => {
  const [value, setValue] = useState(field.value);
  const [t, setT] = useState();

  useEffect(() => {
    setValue(field.value);
  }, [field.value]);

  useEffect(() => {
    if (otherProps.value !== undefined) {
      setValue(otherProps.value);
    }
  }, [otherProps.value]);

  const onChange = (event) => {
    if (readOnly) return;
    const { value } = event.target;
    setValue(value);
    if (!notDelay) {
      if (t) clearTimeout(t);
      setT(
          setTimeout(() => {
            if (otherProps.onChange) {
              otherProps.onChange(value);
            } else {
              setFieldValue(name, value);
            }
          }, 400)
      );
    } else {
      if (otherProps.onChange) {
        otherProps.onChange(value);
      } else {
        setFieldValue(name, value);
      }
    }
  };

  const minDateMessageDefault = i18n.t("validation.invalidDate");
  const maxDateMessageDefault = i18n.t("validation.invalidDate");
  const okLabelDefault = "CHỌN";
  const cancelLabelDefault = "HUỶ";

  const configDateTimePicker = {
    ...field,
    ...otherProps,
    disablePast: !!disablePast,
    disableFuture: !!disableFuture,
    variant: variant || "outlined",
    size: size || "small",
    fullWidth: true,
    value: value ? new Date(value) : null,
    id: name,
    label: false,
    type: "datetime-local",
    onChange,
    InputLabelProps: {
      htmlFor: name,
      shrink: true,
    },
    className: readOnly ? "read-only" : "",
    InputProps: {
      readOnly: readOnly,
    },
    inputProps: {
      readOnly: readOnly,
    },
    minDateMessage: otherProps.minDateMessage || minDateMessageDefault,
    maxDateMessage: otherProps.maxDateMessage || maxDateMessageDefault,
    okLabel: otherProps.okLabel || okLabelDefault,
    cancelLabel: otherProps.cancelLabel || cancelLabelDefault,
  };

  if (meta && meta.touched && meta.error) {
    configDateTimePicker.error = true;
    configDateTimePicker.helperText = meta.error;
  }

  return (
      <div>
        <label htmlFor={name} style={{ fontSize: "1rem" }}>
          {otherProps.label}{" "}
          {otherProps.required ? <span style={{ color: "red" }}> * </span> : null}
        </label>
        <TextField {...configDateTimePicker} />
      </div>
  );
};

const shouldComponentUpdate = (nextProps, currentProps) => {
  return (
      nextProps.name !== currentProps.name ||
      nextProps.value !== currentProps.value ||
      nextProps.onChange !== currentProps.onChange ||
      nextProps.disablePast !== currentProps.disablePast ||
      nextProps.disableFuture !== currentProps.disableFuture ||
      nextProps.label !== currentProps.label ||
      nextProps.required !== currentProps.required ||
      nextProps.disabled !== currentProps.disabled ||
      nextProps.readOnly !== currentProps.readOnly ||
      nextProps.formik.isSubmitting !== currentProps.formik.isSubmitting ||
      Object.keys(nextProps).length !== Object.keys(currentProps).length ||
      getIn(nextProps.formik.values, currentProps.name) !==
      getIn(currentProps.formik.values, currentProps.name) ||
      getIn(nextProps.formik.errors, currentProps.name) !==
      getIn(currentProps.formik.errors, currentProps.name) ||
      getIn(nextProps.formik.touched, currentProps.name) !==
      getIn(currentProps.formik.touched, currentProps.name)
  );
};

export default React.memo(CommonDateTime);
