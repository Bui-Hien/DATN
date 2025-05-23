import React from "react";
import {observer} from "mobx-react-lite";
import {useStore} from "../../stores";
import CommonTable from "../../common/CommonTable";
import {formatVNDMoney} from "../../LocalFunction";

function SalaryResultItemList() {
    const {salaryResultItemStore} = useStore();

    const {
        totalPages,
        searchObject,
        totalElements,
        setPageIndex,
        setPageSize,
        dataList
    } = salaryResultItemStore;

    const baseColumns = [
        {
            accessorKey: "staffCode",
            header: "Mã nhân viên",
        },
        {
            accessorKey: "displayName",
            header: "Tên nhân viên",
        },
    ];


    let columns = [];
    if (
        Array.isArray(dataList) &&
        dataList.length > 0 &&
        Array.isArray(dataList[0]?.salaryResultItemDetails)
    ) {
        columns = dataList[0].salaryResultItemDetails.map(itemDetail => ({
            accessorKey: itemDetail?.salaryTemplateItem?.code || "",
            header: itemDetail?.salaryTemplateItem?.name || "",
            Cell: ({row}) => {
                const value = row.getValue(itemDetail.salaryTemplateItem.code);
                return <span>{formatVNDMoney(value)}</span>;
            }
        }));
    }

    const finalColumns = [...baseColumns, ...columns];

    const data = Array.isArray(dataList)
        ? dataList.map(item => {
            const result = {
                staffCode: item.staff?.staffCode || "",
                displayName: item.staff?.displayName || "",
            };

            if (Array.isArray(item.salaryResultItemDetails)) {
                item.salaryResultItemDetails.forEach(detail => {
                    const key = detail?.salaryTemplateItem?.code;
                    if (key) {
                        result[key] = detail?.value ?? "";
                    }
                });
            }

            return result;
        })
        : [];

    return (
        <CommonTable
            data={data}
            columns={finalColumns}
            totalPages={totalPages}
            enableColumnPinning={true}
            pinnedLeftColumns={['staffCode', 'displayName']}
            pageSize={searchObject.pageSize}
            page={searchObject.pageIndex}
            totalElements={totalElements}
            pageSizeOption={[5, 10, 15]}
            handleChangePage={setPageIndex}
            setRowsPerPage={setPageSize}

        />
    );
}

export default observer(SalaryResultItemList);