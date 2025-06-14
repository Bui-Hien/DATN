import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import {AdministrativeUnitLevel, SalaryPeriodStatus} from "../../LocalConstants";
import {getDate} from "../../LocalFunction";

function RecruitmentRequestList() {
    const {t} = useTranslation();
    const {recruitmentRequestStore} = useStore();

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
    } = recruitmentRequestStore;


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
                t("Mã yêu cầu tuyển dụng"),
        },
        {
            accessorKey: "name",
            header:
                t("Tên yêu cầu tuyển dụng"),
        },
        {
            accessorKey: "proposer",
            header:
                t("Người đề xuất tuyển dụng"),
            Cell: ({row}) => {
                const value = row.original.proposer;
                return <span>{value?.displayName || ""}</span>;
            }
        },
        {
            accessorKey: "proposalDate",
            header:
                t("Ngày đề xuất tuyển dụng"),
            Cell: ({row}) => {
                const value = row.original.proposalDate;
                return <span>{getDate(value)}</span>;
            }
        },
        {
            accessorKey: "position",
            header:
                t("Vị trí tuyển dụng"),
            Cell: ({row}) => {
                const value = row.original.position;
                return <span>{value?.name || ""}</span>;
            }
        },
        {
            accessorKey: "request",
            header:
                t("Yêu cầu tuyển dụng"),
        },
        {
            accessorKey: "description",
            header:
                t("Mô tả yêu cầu tuyển dụng"),
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

export default observer(RecruitmentRequestList);
