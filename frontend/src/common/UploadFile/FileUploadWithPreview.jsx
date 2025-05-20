import React, { useState } from "react";
import { FileUploader } from "react-drag-drop-files";
import { Box, Button, IconButton, Typography } from "@mui/material";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import DeleteIcon from "@mui/icons-material/Delete";
import {saveFile} from "./UploadFileService";
import {toast} from "react-toastify";

const FileUploadWithPreview = (props) => {
    const {
        fileTypes = ["JPG", "PNG", "GIF", "JPEG", "PDF"],
        onUploadSuccess, // callback để truyền kết quả về parent nếu cần
    } = props;

    const [file, setFile] = useState(null);
    const [previewUrl, setPreviewUrl] = useState(null);
    const [uploading, setUploading] = useState(false);
    const [error, setError] = useState(null);

    const handleChange = (file) => {
        setFile(file);
        const fileUrl = URL.createObjectURL(file);
        setPreviewUrl(fileUrl);
        setError(null);
    };

    const clearFile = () => {
        setFile(null);
        setPreviewUrl(null);
        setError(null);
    };

    const handleUpload = async () => {
        if (!file) return;
        setUploading(true);
        setError(null);

        try {
            const res = await saveFile(file); // gửi file lên server
            if (onUploadSuccess) {
                onUploadSuccess(res.data); // gọi callback nếu có
            }
            toast.success("Tải file thành công!");
            clearFile();
        } catch (err) {
            console.error(err);
            toast.error("Tải file thất bại. Vui lòng thử lại.");
            setError("Tải file thất bại");
        } finally {
            setUploading(false);
        }
    };

    const renderPreview = () => {
        if (!file) return null;

        const isImage = file.type.startsWith("image/");
        const isPdf = file.type === "application/pdf";

        return (
            <Box mt={2} display="flex" alignItems="center" flexDirection="column">
                <Box display="flex" alignItems="center">
                    {isImage ? (
                        <img
                            src={previewUrl}
                            alt="Preview"
                            style={{ maxWidth: 300, maxHeight: 300, borderRadius: 8 }}
                        />
                    ) : isPdf ? (
                        <Box display="flex" flexDirection="column" alignItems="center">
                            <PictureAsPdfIcon fontSize="large" color="error" />
                            <Typography>{file.name}</Typography>
                        </Box>
                    ) : (
                        <Typography>Không hỗ trợ xem trước loại file này.</Typography>
                    )}
                    <IconButton onClick={clearFile} color="error">
                        <DeleteIcon />
                    </IconButton>
                </Box>
            </Box>
        );
    };

    return (
        <Box>
            <Typography variant="h6" gutterBottom>
                Tải lên ảnh hoặc PDF
            </Typography>
            <FileUploader
                handleChange={handleChange}
                name="file"
                types={fileTypes}
                label="Kéo & thả hoặc click để chọn file"
            />
            {renderPreview()}
            {error && (
                <Typography mt={1} color="error">
                    {error}
                </Typography>
            )}
            {file && (
                <Box mt={2}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleUpload}
                        disabled={uploading}
                    >
                        {uploading ? "Đang tải..." : "Upload"}
                    </Button>
                </Box>
            )}
        </Box>
    );
};

export default FileUploadWithPreview;
