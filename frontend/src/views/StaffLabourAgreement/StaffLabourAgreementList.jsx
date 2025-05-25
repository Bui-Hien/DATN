import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import {getDate} from "../../LocalFunction";
import {StaffLabourAgreementStatus} from "../../LocalConstants";
import VisibilityIcon from '@mui/icons-material/Visibility';
function StaffLabourAgreementList() {
    const {t} = useTranslation();
    const {staffLabourAgreementStore, staffStore} = useStore();

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
    } = staffLabourAgreementStore;

    const {isProfile} = staffStore;


    const columns = [
        {
            accessorKey: "action",
            header: t("Hành động"),
            Cell: ({row}) => (
                <div className="flex flex-middle justify-center">
                    {!isProfile && (
                        <Tooltip
                            className={"cursor-pointer"}
                            title={t("Cập nhật thông tin")}
                            placement="top">
                            <EditIcon
                                onClick={() => handleOpenCreateEdit(row.original.id)}
                            />
                        </Tooltip>
                    )}
                    {!isProfile && (
                        <Tooltip title={t("Xóa")} placement="top">
                            <DeleteIcon
                                className="cursor-pointer ml-4"
                                onClick={() => handleDelete(row.original)}
                            />
                        </Tooltip>
                    )}
                    {isProfile && (
                        <Tooltip title={t("Xem chi tiết")} placement="top">
                            <VisibilityIcon
                                className="cursor-pointer ml-4"
                                onClick={() => handleOpenCreateEdit(row.original.id)}
                            />
                        </Tooltip>
                    )}

                </div>
            ),
        },
        {
            accessorKey: "labourAgreementNumber",
            header:
                t("Số hợp đồng"),
        },
        {
            accessorKey: "contractType",
            header:
                t("Loại hợp đồng"),
            Cell: ({row}) => {
                const statusValue = row.original?.contractType;
                return <span>{StaffLabourAgreementStatus.getListData().find(elem => elem.value === statusValue)?.name || t("Không xác định")}</span>;
            },
        },

        {
            accessorKey: "startDate",
            header:
                t("Ngày ký"),
            Cell: ({row}) => <span>{getDate(row.original?.startDate)}</span>

        },
        {
            accessorKey: "durationMonths",
            header:
                t("Số tháng"),
        },
        {
            accessorKey: "workingHour",
            header:
                t("Số giờ làm việc tối thiểu trong ngày"),
        },
        {
            accessorKey: "workingHourWeekMin",
            header:
                t("Số giờ làm việc tối thiểu trong tuần"),
        },
        {
            accessorKey: "salary",
            header:
                t("Lương cơ bản"),
        },
        {
            accessorKey: "signedDate",
            header:
                t("Ngày hết hạn"),
            Cell: ({row}) => <span>{getDate(row.original?.signedDate)}</span>

        },
        {
            accessorKey: "agreementStatus",
            header:
                t("Trạng thái hợp đồng"),
            Cell: ({row}) => {
                const statusValue = row.original?.agreementStatus;
                return <span>{StaffLabourAgreementStatus.getListData().find(elem => elem.value === statusValue)?.name || t("Không xác định")}</span>;
            },
        },
    ];

    return (
        <CommonTable
            data={dataList}
            columns={columns}
            selection={!isProfile}
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

export default observer(StaffLabourAgreementList);
