import React from 'react'
import { flexRender, getCoreRowModel, getSortedRowModel, SortingState, useReactTable } from '@tanstack/react-table';

import { GetLogs_getLogs as Log } from '../../graphql/types/getLogs';

import THeader from './header/theader';
import THeaderGroup from './header/theaderGroup';
import THead from './header/thead';
import TBody from './body/tbody';
import TRow from './body/trow';
import TData from './body/tdata';


import { StyledTable } from './table.styled';

interface Props {
    data: Log[],
    columns: any,
    columnGrid: string,
    entities: string,
    onEntryDoubleClick?: (id: string) => void,
}

const Table:React.FC<Props> = (props: Props) => {

    const [sorting, setSorting] = React.useState<SortingState>([])

    const table = useReactTable({
        data: props.data,
        columns: props.columns,
        state: {
            sorting,
        },
        onSortingChange: setSorting,
        getCoreRowModel: getCoreRowModel(),
        getSortedRowModel: getSortedRowModel(),
    });


    const selectionHelper = (isAllSelected: boolean, isSomeSelected: boolean) => {
        if (isAllSelected) {
            return 1;
        } else if (isSomeSelected) {
            return 2;
        } else {
            return 0;
        }
    }

    return (
        <StyledTable>
            <THeader entries={props.data.length} selected={table.getSelectedRowModel().rows.length} toggleSelectAll={table.toggleAllRowsSelected} selectionState={selectionHelper(table.getIsAllRowsSelected(), table.getIsSomeRowsSelected())} entities={props.entities}>
                {table.getHeaderGroups().map(headerGroup => (
                    <THeaderGroup key={headerGroup.id} columnGrid={props.columnGrid}>
                        {headerGroup.headers.map(header => (
                            <THead key={header.id} isSortable={header.column.getCanSort()} isSorted={header.column.getIsSorted()} toggleSort={() => header.column.toggleSorting()}>
                                {header.isPlaceholder
                                ? null
                                : flexRender(
                                    header.column.columnDef.header,
                                    header.getContext()
                                )}
                            </THead>
                        ))}
                    </THeaderGroup>
                ))}
            </THeader>
            <TBody>
                {table.getRowModel().rows.map(row => (
                    <TRow key={row.id} isSelected={row.getIsSelected()} columnGrid={props.columnGrid} onDoubleClick={() => props.onEntryDoubleClick ? props.onEntryDoubleClick(row.original.timestamp as string) : null} onClick={(e) => {
                        if (table.getSelectedRowModel().rows.length != 0 && !e.ctrlKey) {
                            table.getSelectedRowModel().rows.forEach((r) => {
                                if (r.id != row.id) {
                                    r.toggleSelected();
                                }
                            });
                        }
                        if (!row.getIsSelected() || e.ctrlKey) {
                            row.toggleSelected();
                        }
                    }}>
                        {row.getVisibleCells().map(cell => (
                            <TData key={cell.id}>
                                {flexRender(cell.column.columnDef.cell, cell.getContext())}
                            </TData>
                        ))}
                    </TRow>
                ))}
            </TBody>
        </StyledTable>
    )
}

export default Table