import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import handleDownload from "../../common/UploadFile/DowloadFile";

function CertificateList() {
    const {t} = useTranslation();
    const {certificateStore} = useStore();

    const {
        handleOpenCreateEdit,
        totalPages,
        handleDelete,
        dataList,
        searchObject,
        totalElements,
        setPageIndex,
        setPageSize,
        handleSelectListDelete
    } = certificateStore;


    const columns = [
        {
            accessorKey: "action",
            header: t("Hành động"),
            Cell: ({row}) => (
                <div className="flex flex-middle justify-center">
                    <Tooltip
                        className={"cursor-pointer"}
                        title={t("Cập nhật thông tin")}
                        placement="top">
                        <EditIcon
                            onClick={() => handleOpenCreateEdit(row.original.id)}
                        />
                    </Tooltip>

                    <Tooltip title={t("Xóa")} placement="top">
                        <DeleteIcon
                            className="cursor-pointer ml-4"
                            onClick={() => handleDelete(row.original)}
                        />
                    </Tooltip>
                </div>
            ),
        },
        {
            accessorKey: "code",
            header:
                t("Mã chứng chỉ"),
        },
        {
            accessorKey: "name",
            header:
                t("Tên chứng chỉ"),
        },
        {
            accessorKey: "description",
            header:
                t("Mô tả chứng chỉ"),
        },
        {
            accessorKey: "certificateFile",
            header:
                t("Tải chứng chỉ"),
            Cell: ({row}) => (
                <div className="flex justify-center align-center">
                    <FileDownloadIcon
                        onClick={() => {
                            const fileId = row.original?.certificateFile?.id;
                            if (fileId) {
                                handleDownload(row.original?.certificateFile);
                            }
                        }}
                        style={{
                            cursor: row.original?.certificateFile?.id ? 'pointer' : 'default',
                            opacity: row.original?.certificateFile?.id ? 1 : 0.3,
                        }}
                    />
                </div>
            )
        },
    ];

    return (
        <CommonTable
            data={dataList}
            columns={columns}
            selection={true}
            nonePagination={false}
            totalPages={totalPages}
            pageSize={searchObject.pageSize}
            page={searchObject.pageIndex}
            totalElements={totalElements}
            pageSizeOption={[5, 10, 15]}
            handleChangePage={setPageIndex}
            setRowsPerPage={setPageSize}
            handleSelectList={handleSelectListDelete}
        />
    );
}

export default observer(CertificateList);
