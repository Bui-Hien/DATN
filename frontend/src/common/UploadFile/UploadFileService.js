import api from "../../axiosCustom";

const API_PATH = "/api/file-description";

export const saveFile = (file) => {
    const formData = new FormData();
    formData.append("file", file);

    return api.post(`${API_PATH}/save-file`, formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    });
};