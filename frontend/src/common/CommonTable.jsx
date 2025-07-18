import React, {useEffect} from 'react';
import {flexRender, MRT_TableBodyCellValue, useMaterialReactTable,} from 'material-react-table';
import {
    MenuItem,
    Pagination,
    Select,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
} from '@mui/material';

const CommonTable = (props) => {
    const {
        handleSelectList,
        data = [],
        columns = [],
        selection = false,
        nonePagination = false,
        colParent = false,
        defaultExpanded = false,
        totalPages,
        handleChangePage,
        setRowsPerPage,
        pageSize = 10,
        pageSizeOption = [10, 15, 25, 30],
        totalElements,
        enableColumnPinning = false,
        pinnedLeftColumns = [], // thêm prop này
        page = 1,
    } = props;

    const table = useMaterialReactTable({
        enableColumnPinning: enableColumnPinning,
        columns,
        data,
        enableRowSelection: selection,
        enablePagination: !nonePagination,
        manualPagination: !!handleChangePage,
        pageCount: totalPages,
        initialState: {
            pagination: {
                pageIndex: page,
                pageSize,
            },
            expanded: colParent ? {[0]: defaultExpanded} : {},
            showGlobalFilter: false,
            columnPinning: {
                left: pinnedLeftColumns,
            },
        },
        muiPaginationProps: {
            rowsPerPageOptions: pageSizeOption,
            variant: 'outlined',
            onPageChange: (e, newPage) => {
                if (handleChangePage) handleChangePage(newPage);
            },
            onRowsPerPageChange: (e) => {
                if (setRowsPerPage) setRowsPerPage(Number(e.target.value));
            },
            rowsPerPage: pageSize,
            page,
            count: totalElements,
        },
        paginationDisplayMode: 'pages',
        enableExpanding: colParent,
    });
    useEffect(() => {
        if (selection) {
            table.resetRowSelection();
        }
    }, [data]);
    useEffect(() => {
        if (selection) {
            const selectedRows = table.getSelectedRowModel().rows.map(
                (row) => row.original
            );
            handleSelectList?.(selectedRows);
        }
    }, [table.getState().rowSelection, selection, handleSelectList]);

    const [columnNumber, setColumnNumber] = React.useState(columns.length);
    useEffect(() => {
        if (selection) {
            setColumnNumber(prev => prev + 1);
        }
        if (colParent) {
            setColumnNumber(prev => prev + 1);
        }
    }, [selection, colParent]);

    const getPinnedLeft = (column) => {
        if (!table || !column || !column.getIsPinned?.() || column.getIsPinned() !== 'left') return undefined;

        const allColumns = table.getAllLeafColumns();
        let leftOffset = 0;
        for (const col of allColumns) {
            if (col.id === column.id) break;
            if (col.getIsPinned?.() === 'left') {
                leftOffset += 150;
            }
        }
        return leftOffset;
    };
    return (
        <Stack className={"gap-2 my-2"}>
            <TableContainer className="border border-gray-300 rounded-xl overflow-hidden">
                <Table
                    className="w-full"
                    style={{borderCollapse: 'collapse'}}
                >
                    <TableHead className="bg-gray-100">
                        {table.getHeaderGroups().map((headerGroup) => (
                            <TableRow key={headerGroup.id}>
                                {headerGroup.headers.map((header, index) => (
                                    <TableCell
                                        align="center"
                                        variant="head"
                                        key={header.id}

                                        className={`${header.column.id === 'mrt-row-select' ? "min-w-[50px]" : colParent && index === 0 ? "min-w-[50px]" : "min-w-[150px]"} font-semibold text-sm text-gray-700 border border-gray-300 py-3`}
                                        style={{
                                            position: header.column.getIsPinned?.() === 'left' ? 'sticky' : undefined,
                                            left: getPinnedLeft(header.column),
                                            zIndex: 10,
                                            backgroundColor: 'white',
                                        }}
                                    >
                                        {header.isPlaceholder
                                            ? null
                                            : flexRender(
                                                header.column.columnDef.Header ??
                                                header.column.columnDef.header,
                                                header.getContext()
                                            )}
                                    </TableCell>
                                ))}
                            </TableRow>
                        ))}
                    </TableHead>
                    <TableBody>
                        {data.length > 0 ? (
                            table.getRowModel().rows.map((row, rowIndex) => (
                                <TableRow
                                    key={row.id}
                                    selected={row.getIsSelected()}
                                    className="hover:bg-gray-50 transition-colors"
                                >
                                    {row.getVisibleCells().map((cell, index) => (
                                        <TableCell
                                            align={cell.column.id === 'mrt-row-select' ? 'center' : 'left'}
                                            variant="body"
                                            key={cell.id}
                                            className={`${cell.column.id === 'mrt-row-select' ? "min-w-[50px]" : colParent && index === 0 ? "min-w-[50px]" : "min-w-[150px]"} text-sm text-gray-800 border border-gray-300 py-2`}
                                            style={{
                                                position: cell.column.getIsPinned?.() === 'left' ? 'sticky' : undefined,
                                                left: getPinnedLeft(cell.column),
                                                zIndex: 10,
                                                backgroundColor: 'white',
                                            }}
                                        >
                                            <MRT_TableBodyCellValue
                                                cell={cell}
                                                table={table}
                                                staticRowIndex={rowIndex}
                                            />
                                        </TableCell>
                                    ))}
                                </TableRow>
                            ))
                        ) : (
                            <TableRow>
                                <TableCell
                                    align="center"
                                    colSpan={columnNumber}
                                    className="text-sm text-gray-500 border border-gray-300 py-5"
                                >
                                    Không có dữ liệu
                                </TableCell>
                            </TableRow>
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            {
                !nonePagination && totalElements > 0 && (
                    <Stack
                        direction="row"
                        alignItems="center"
                        justifyContent="space-between"
                        sx={{px: 2}}
                        spacing={2}
                    >
                        <Typography variant="body2">
                            Tổng cộng: {totalElements} bản ghi
                        </Typography>

                        <Stack direction="row" spacing={2} alignItems="center">
                            <Typography variant="body2">Số dòng/trang:</Typography>
                            <Select
                                value={pageSize}
                                onChange={(e) => setRowsPerPage?.(Number(e.target.value))}
                                size="small"
                            >
                                {pageSizeOption.map((option) => (
                                    <MenuItem key={option} value={option}>
                                        {option}
                                    </MenuItem>
                                ))}
                            </Select>

                            <Pagination
                                count={totalPages}
                                page={page}
                                onChange={(e, value) => handleChangePage?.(value)}
                                color="primary"
                                shape="rounded"
                                size="small"
                            />
                        </Stack>
                    </Stack>
                )
            }
        </Stack>
    )
        ;
};

export default React.memo(CommonTable);