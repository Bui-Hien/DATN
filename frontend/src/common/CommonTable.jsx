import React, { useEffect } from 'react';
import {
  flexRender,
  MRT_TableBodyCellValue,
  useMaterialReactTable,
} from 'material-react-table';
import {
  Stack,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Pagination,
  Select,
  MenuItem,
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
      expanded: colParent ? { [0]: defaultExpanded } : {},
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

  return (
      <Stack sx={{ m: '2rem 0' }} spacing={2}>
        <TableContainer>
          <Table>
            <TableHead>
              {table.getHeaderGroups().map((headerGroup) => (
                  <TableRow key={headerGroup.id}>
                    {headerGroup.headers.map((header) => (
                        <TableCell align="center" variant="head" key={header.id}>
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
              {table.getRowModel().rows.map((row, rowIndex) => (
                  <TableRow key={row.id} selected={row.getIsSelected()}>
                    {row.getVisibleCells().map((cell) => (
                        <TableCell align="center" variant="body" key={cell.id}>
                          <MRT_TableBodyCellValue
                              cell={cell}
                              table={table}
                              staticRowIndex={rowIndex}
                          />
                        </TableCell>
                    ))}
                  </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>

        {!nonePagination && totalElements > 0 && (
            <Stack
                direction="row"
                alignItems="center"
                justifyContent="space-between"
                sx={{ px: 2 }}
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
                    page={page + 1}
                    onChange={(e, value) => handleChangePage?.(value - 1)}
                    color="primary"
                    shape="rounded"
                    size="small"
                />
              </Stack>
            </Stack>
        )}
      </Stack>
  );
};

export default Example;
