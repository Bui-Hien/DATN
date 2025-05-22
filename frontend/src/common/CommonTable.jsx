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

const Example = (props) => {
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
        page = 1,
    } = props;

    const table = useMaterialReactTable({
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
                                {headerGroup.headers.map((header) => (
                                    <TableCell
                                        align="center"
                                        variant="head"
                                        key={header.id}
                                        className={`${header.column.id === 'mrt-row-select' ? "" : "min-w-[150px]"} font-semibold text-sm text-gray-700 border border-gray-300 py-3`}
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
                                    {row.getVisibleCells().map((cell) => (
                                        <TableCell
                                            align={cell.column.id === 'mrt-row-select' ? 'center' : 'left'}
                                            variant="body"
                                            key={cell.id}
                                            className={`${cell.column.id === 'mrt-row-select' ? "" : "min-w-[150px]"} text-sm text-gray-800 border border-gray-300 py-2`}
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

export default Example;
