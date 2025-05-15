import React, { useEffect, useState, Fragment } from "react";
import TextField from "@mui/material/TextField";
import CircularProgress from "@mui/material/CircularProgress";
import { useField, useFormikContext } from "formik";
import {Autocomplete} from "@mui/material";

const CommonAsyncAutocomplete = ({
                                    name,
                                    api,
                                    variant,
                                    size,
                                    searchObject,
                                    label,
                                    shrink = false,
                                    required = false,
                                    placeholder = "",
                                    getOptionDisabled = () => false,
                                    oldStyle = false,
                                    readOnly = false,
                                    ...otherProps
                                  }) => {
  const { setFieldValue } = useFormikContext();
  const [field, meta] = useField(name);
  const [open, setOpen] = useState(false);
  const [options, setOptions] = useState([]);
  const loading = open && options.length === 0;

  useEffect(() => {
    let active = true;

    if (!loading) {
      return undefined;
    }

    (async () => {
      try {
        const response = searchObject != null ? await api(searchObject) : await api();
        if (active && response.data) {
          const data = response.data.content || response.data;
          setOptions(Array.isArray(data) ? data : []);
        }
      } catch (error) {
        console.error("Error fetching data:", error);
        setOptions([]);
      }
    })();

    return () => {
      active = false;
    };
  }, [api, loading, searchObject]);

  useEffect(() => {
    if (!open) {
      setOptions([]);
    }
  }, [open]);

  const handleChange = (_, value) => {
    if (readOnly) return;
    setFieldValue(name, value || null);
  };

  const defaultGetOptionLabel = (option) =>
      option[otherProps.displayName ? otherProps.displayName : "name"] || "";

  const config = {
    ...field,
    ...otherProps,
    id: name,
    open: readOnly ? false : open,
    size: size || "small",
    className: `${oldStyle ? "" : "input-container"} ${readOnly ? "read-only" : ""}`,
    onOpen: () => {
      if (!readOnly) setOpen(true);
    },
    onClose: () => setOpen(false),
    onChange: handleChange,
    isOptionEqualToValue: (option, value) => option.id === value.id,
    getOptionLabel: otherProps.getOptionLabel || defaultGetOptionLabel,
    getOptionDisabled: readOnly ? () => true : getOptionDisabled,
    options,
    loading: loading && !readOnly,
    renderInput: (params) => (
        <>
          {label && (
              <label
                  htmlFor={name}
                  className={oldStyle ? "old-label" : "label-container"}
              >
                {label} {required && <span style={{ color: "red" }}> * </span>}
              </label>
          )}
          <TextField
              {...params}
              variant={variant || "outlined"}
              placeholder={placeholder}
              InputProps={{
                ...params.InputProps,
                readOnly,
                sx: readOnly
                    ? {
                      color: "rgba(0, 0, 0, 0.87)",
                      backgroundColor: "rgba(0, 0, 0, 0.02)",
                      opacity: 1,
                    }
                    : {},
                endAdornment: (
                    <Fragment>
                      {loading && !readOnly ? (
                          <CircularProgress color="inherit" size={20} />
                      ) : null}
                      {params.InputProps.endAdornment}
                    </Fragment>
                ),
              }}
              inputProps={{
                ...params.inputProps,
                readOnly,
                style: readOnly
                    ? {
                      color: "rgba(0, 0, 0, 0.87)",
                      cursor: "text",
                      opacity: 1,
                    }
                    : {},
              }}
          />
        </>
    ),
  };

  if (meta.touched && meta.error) {
    config.error = true;
    config.helperText = meta.error;
  }

  return <Autocomplete {...config} />;
};

export default CommonAsyncAutocomplete;
