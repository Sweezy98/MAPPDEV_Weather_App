export interface GetLogs_getLogs {
    timestamp: string | null;
    level: string | null;
    trace: string | null;
    message: string | null;
}

export interface GetLogsType {
    getLogs: GetLogs_getLogs[] | null;
}

export interface GetLogsVariables {
    limit: number | null;
    offset: number | null;
}