import {downloadFile} from "./UploadFileService";
import {toast} from "react-toastify";

const handleDownload = async (id, fileDisplayName = "file") => {
    if (!id) return;

    try {
        const response = await downloadFile(id); // có thể là id hoặc file name
        const blob = new Blob([response.data]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", fileDisplayName); // tên khi lưu
        document.body.appendChild(link);
        link.click();
        link.remove();
        URL.revokeObjectURL(url);
    } catch (err) {
        console.error(err);
        toast.error("Không thể tải file");
    }
};

export default handleDownload;
