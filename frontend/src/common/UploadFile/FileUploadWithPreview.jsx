import React, {useState} from "react";
import {FileUploader} from "react-drag-drop-files";
import {Typography} from "@mui/material";
import {saveFile} from "./UploadFileService";
import {toast} from "react-toastify";
import CommonPopupV2 from "../CommonPopupV2";
import {CircularProgress} from "@mui/material"; // thêm import này ở đầu

const FileUploadWithPreview = (props) => {
    const {
        fileTypes = ["JPG", "PNG", "GIF", "JPEG", "PDF"],
        onUploadSuccess, // callback để truyền kết quả về parent nếu cần
        title = "Tải lên ảnh hoặc PDF",
        open,
        handleClose
    } = props;

    const [error, setError] = useState(null);
    const [uploading, setUploading] = useState(false);
    const handleChange = async (file) => {
        try {
            setUploading(true);
            const res = await saveFile(file); // gửi file lên server
            if (onUploadSuccess) {
                onUploadSuccess(res?.data?.data); // gọi callback nếu có
                console.log(res?.data?.data);
            }
            toast.success("Tải file thành công!");
            if (handleClose) handleClose(); // GỌI handleClose ở đây
        } catch (err) {
            console.error(err);
            toast.error("Tải file thất bại. Vui lòng thử lại.");
            setError("Tải file thất bại");
        } finally {
            setUploading(false);
        }
    };

    return (
        <CommonPopupV2
            size="xs"
            scroll={"paper"}
            open={open}
            noDialogContent
            title={title}
            onClosePopup={handleClose}
            noIcon={true}
        >
            <div className="w-full h-[250px] mx-auto">
                <div className="p-4 h-full w-full">
                    {uploading ? (
                        <div className={"w-full h-full flex justify-center items-center"}>
                            <CircularProgress/>
                        </div>
                    ) : (
                        <FileUploader
                            handleChange={handleChange}
                            name="file"
                            types={fileTypes}
                            label="Kéo & thả hoặc click để chọn file, max size 5MB"
                            classes={"!h-full !w-full"}
                            maxSize={5}
                        />
                    )}
                </div>
                {error && (
                    <Typography mt={1} color="error">
                        {error}
                    </Typography>
                )}
            </div>
        </CommonPopupV2>
    );
};

export default FileUploadWithPreview;
