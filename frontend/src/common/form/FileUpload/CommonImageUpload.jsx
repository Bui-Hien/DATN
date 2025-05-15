import React, {useRef, useState} from "react";
import {Avatar, Button, Typography,} from "@mui/material"; // MUI v5 import
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import classnames from "classnames";
// import your custom style or sx styles

const ImageInput = (props) => {
  const {
    errorMessage,
    title,
    imagePath,
    nameStaff,
    field,
    onChange,
    disabled,
    wrapperAvatarStyle,
    // classes, // nếu dùng styled thì ko cần
  } = props;

  const fileUpload = useRef(null);
  const [file, setFile] = useState();
  const [imagePreviewUrl, setImagePreviewUrl] = useState();

  const showFileUpload = () => {
    fileUpload.current?.click();
  };

  const handleImageChange = (e) => {
    e.preventDefault();
    const selectedFile = e.target.files[0];

    if (selectedFile) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setFile(selectedFile);
        setImagePreviewUrl(reader.result);
      };
      reader.readAsDataURL(selectedFile);

      onChange(field.name, selectedFile);
    }
  };

  const avatarStyle = classnames(
      // your custom class logic here, or sx prop
  );

  const renderAvatar = () => {
    if (disabled) {
      return (
          <img
              src={imagePath} // add your url logic here
              style={wrapperAvatarStyle}
              alt="Avatar"
          />
      );
    }

    return (
        <Avatar
            onClick={showFileUpload}
            style={wrapperAvatarStyle}
            // add sx or className here
        >
          {/* Your CommonAvatar or fallback */}
        </Avatar>
    );
  };

  return (
      <div>
        <input
            id={field.name}
            name={field.name}
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            ref={fileUpload}
            style={{ display: "none" }}
        />

        {title && (
            <Typography variant="h5" gutterBottom>
              {title}
            </Typography>
        )}

        {renderAvatar()}

        {!disabled && (
            <Button
                variant="contained"
                onClick={showFileUpload}
                startIcon={<CloudUploadIcon />}
            >
              Chọn ảnh
            </Button>
        )}

        {errorMessage && (
            <Typography color="error" variant="caption">
              {errorMessage}
            </Typography>
        )}
      </div>
  );
};

export default ImageInput;
