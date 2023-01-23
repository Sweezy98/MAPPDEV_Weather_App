import React from 'react';
import { createColumnHelper, Row } from '@tanstack/react-table';

import { GetLogs_getLogs as Log } from '../../graphql/types/getLogs';

import LogLevelDisplay from '../../components/LogLevelDisplay/LogLevelDisplay';

const columnHelper = createColumnHelper<Log>();

export const columns = [
   columnHelper.accessor('timestamp', {
      header: 'Timestamp',
      size: 100,
      enableSorting: true,
      enableResizing: true,
      cell: info => <p>{info.getValue()}</p>,     
   }),
   columnHelper.accessor('level', {
      header: 'Level',
      minSize: 100,
      maxSize: 100,
      enableSorting: true,
      enableResizing: true,
      cell: info => <LogLevelDisplay loglevel={info.getValue()} />,     
   }),
   columnHelper.accessor('trace', {
      header: 'Trace',
      minSize: 120,
      maxSize: 200,
      enableSorting: true,
      enableResizing: true,
      cell: info => <p>{info.getValue()}</p>,     
   }),
   columnHelper.accessor('message', {
      header: 'Message',
      minSize: 130,
      maxSize: 300,
      enableSorting: false,
      enableResizing: true,
      cell: info => <p>{info.getValue()}</p>,     
   })
]

export const ColumnGrid = "220px 120px 300px minmax(200px, 1fr)";


export default columns