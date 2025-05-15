import React, {useRef, useState} from "react";
import PropTypes from "prop-types";
import {Box, Button, List, ListItem, ListItemText, Typography,} from "@mui/material";

const CommonFileUpload = ({
                             onFileChange,
                             multiple = false,
                             label = "Upload File",
                             name,
                             disabled = false,
                           }) => {
  const inputRef = useRef();
  const [selectedFiles, setSelectedFiles] = useState([]);

  const handleChange = (event) => {
    const files = Array.from(event.target.files);
    setSelectedFiles(files);
    if (onFileChange) {
      onFileChange(files);
    }
  };

  const handleButtonClick = () => {
    if (!disabled && inputRef.current) {
      inputRef.current.click();
    }
  };

  return (
      <Box>
        <Typography variant="subtitle1" sx={{ mb: 1 }}>
          {label}
        </Typography>
        <input
            ref={inputRef}
            type="file"
            name={name}
            hidden
            multiple={multiple}
            onChange={handleChange}
            disabled={disabled}
        />
        <Button
            variant="contained"
            color="primary"
            onClick={handleButtonClick}
            disabled={disabled}
        >
          {multiple ? "Select Files" : "Select File"}
        </Button>

        {selectedFiles.length > 0 && (
            <Box mt={2}>
              <Typography variant="body2">Selected Files:</Typography>
              <List dense>
                {selectedFiles.map((file, index) => (
                    <ListItem key={index}>
                      <ListItemText primary={file.name} />
                    </ListItem>
                ))}
              </List>
            </Box>
        )}
      </Box>
  );
};

CommonFileUpload.propTypes = {
  onFileChange: PropTypes.func.isRequired,
  multiple: PropTypes.bool,
  label: PropTypes.string,
  name: PropTypes.string,
  disabled: PropTypes.bool,
};

export default CommonFileUpload;
