import React from "react";
import {useTranslation} from "react-i18next";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import {Tooltip} from "@mui/material";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import CommonTable from "../../common/CommonTable";
import {getDate} from "../../LocalFunction";
import CheckIcon from "@mui/icons-material/Check";

function PersonBankAccountList() {
    const {t} = useTranslation();
    const {personBankAccountStore} = useStore();

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
    } = personBankAccountStore;


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
            accessorKey: "bank",
            header:
                t("Tên ngân hàng"),
            Cell: ({row}) => <span>{row.original?.bank?.name || ""}</span>
        },
        {
            accessorKey: "bankAccountName",
            header:
                t("Tên tài khoản"),
        },
        {
            accessorKey: "bankAccountNumber",
            header:
                t("Số tài khoản"),
        },
        {
            accessorKey: "bankBranch",
            header:
                t("Chi nhánh"),
        },
        {
            accessorKey: "isMain",
            header:
                t("Là tài khoản chính"),
            Cell: ({row}) => {
                const isMain = row.original?.isMain;
                return <span className={"flex justify-center"}>{isMain ?
                    <CheckIcon fontSize="small" style={{color: "green"}}/> : ""}</span>;
            }
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

export default observer(PersonBankAccountList);
