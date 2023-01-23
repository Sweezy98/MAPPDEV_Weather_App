import React, { useState, useEffect } from 'react';
import { useQuery } from '@apollo/client';

import { StyledLogs, StyledTableContainer } from './Logs.styled';

import Table from '../../components/Table/table'

import { GetLogsType, GetLogsVariables, GetLogs_getLogs } from '../../graphql/types/getLogs';
import { getLogs } from '../../graphql/Queries';

import Columns, { ColumnGrid } from './columns';

interface Props {

}

export const Logs: React.FC<Props> = (props: Props) => {
    // usestate for logs, type is GetLogs_getLogs
    const [logs, setLogs] = useState<GetLogs_getLogs[]>([]);

    const { data: response, error, refetch } = useQuery<GetLogsType, GetLogsVariables>(getLogs, {
        fetchPolicy: 'no-cache',
        variables: {
            limit: 100,
            offset: 0
        }
    });

    // refetch every 5 seconds
    // useEffect(() => {
    //     const interval = setInterval(() => {
    //         refetch();
    //     }, 5000);
    //     return () => clearInterval(interval);
    // }, [refetch]);


    useEffect(() => {
        if (response) {
            setLogs(response.getLogs!!);
        }
    }, [response]);

    if (error) {
        //dispatch(clientsError(error.message));
        console.log(error);
    }

    return (
        <StyledLogs className="content">
            <StyledTableContainer>
                <Table columns={Columns} data={logs} columnGrid={ColumnGrid} entities="Entries" />
            </StyledTableContainer>
        </StyledLogs>
    )
}

export default Logs
