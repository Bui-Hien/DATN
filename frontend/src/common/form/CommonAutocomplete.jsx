import React, { useEffect, useState } from "react";
import TextField from "@mui/material/TextField";
import { useFormikContext, useField } from "formik";
import {Autocomplete, InputLabel} from "@mui/material";

const CommonAutocomplete = ({
                               name,
                               options,
                               displayData,
                               variant,
                               size,
                               isObject,
                               properties,
                               label = "",
                               getOptionLabel,
                               handleChange,
                               defaultValue,
                               validate,
                               oldStyle,
                               readOnly = false,
                               ...otherProps
                             }) => {
  const { setFieldValue } = useFormikContext();
  const [field, meta] = useField(name);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    if (defaultValue) {
      if (handleChange) {
        handleChange(null, defaultValue);
      } else {
        defaultHandleChange(null, defaultValue);
      }
    }
  }, [defaultValue]); // eslint-disable-line react-hooks/exhaustive-deps

  const defaultHandleChange = (_, value) => {
    if (readOnly) return;
    if (isObject === false) {
      setFieldValue(name, value?.value ?? null);
    } else {
      setFieldValue(name, value ?? null);
    }
  };

  const defaultGetOptionLabel = (option) =>
      option?.[displayData ?? "name"] ?? "";

  const configAutocomplete = {
    ...field,
    ...otherProps,
    id: name,
    size: size || "small",
    className: `${oldStyle ? "" : "input-container"} ${
        readOnly ? "read-only" : ""
    }`,
    options: options || [],
    open: readOnly ? false : open,
    onOpen: () => {
      if (!readOnly) setOpen(true);
    },
    onClose: () => setOpen(false),
    getOptionLabel: getOptionLabel || defaultGetOptionLabel,
    onChange: handleChange || defaultHandleChange,
    isOptionEqualToValue: (option, value) => option?.id === value?.id,
    getOptionDisabled: readOnly ? () => true : undefined,
    renderInput: (params) => {
      // Fix hiển thị value khi dùng formik (nếu inputProps.value bị rỗng)
      if (field?.value && !params?.inputProps?.value) {
        params.inputProps.value = (getOptionLabel || defaultGetOptionLabel)(
            field.value
        );
      }
      return (
          <TextField
              {...params}
              variant={variant || "outlined"}
              className={readOnly ? "read-only" : ""}
              error={!!(meta.touched && meta.error)}
              helperText={meta.touched && meta.error ? meta.error : ""}
              InputProps={{
                ...params.InputProps,
                readOnly,
              }}
              inputProps={{
                ...params.inputProps,
                readOnly,
              }}
          />
      );
    },
  };

  return (
      <>
        {label && (
            <InputLabel
                htmlFor={name}
                className={oldStyle ? "old-label" : "label-container"}
            >
              {label} {validate && <span className="text-danger"> * </span>}
            </InputLabel>
        )}
        <Autocomplete {...configAutocomplete} />
      </>
  );
};

export default CommonAutocomplete;
