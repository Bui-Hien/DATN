import {toast} from "react-toastify";
import {API_ENDPOINT} from "../../appConfig";
import api from "../../axiosCustom";

const handleDownload = async (fileData) => {
    if (!fileData?.filePath) {
        toast.error("Không có thông tin file");
        return;
    }

    try {
        const fileUrl = `${API_ENDPOINT}/${fileData.filePath}`;

        // Gọi API tải file dạng blob
        const response = await api.get(fileUrl, {
            responseType: "blob",
        });

        // Tạo một blob và tự động tải về
        const blob = new Blob([response.data]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;

        // Tên file tải về = name.extension, ví dụ: bang_cap.png
        const downloadName = fileData.name || `file.${fileData.extension || "dat"}`;
        link.setAttribute("download", downloadName);

        document.body.appendChild(link);
        link.click();
        link.remove();
        window.URL.revokeObjectURL(url);
    } catch (err) {
        console.error("Download failed:", err);
        toast.error("Không thể tải file");
    }
};
export default handleDownload;
