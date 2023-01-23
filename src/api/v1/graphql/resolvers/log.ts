import { QueryResolvers, MutationResolvers, QueryGetLogsArgs, LogResolvers } from '../types/resolvers-types';

import Logger from "../../../../helpers/logger";

import Log from "../../../../models/log.model";

const logger = new Logger('Resolvers.Log');


export const queries: QueryResolvers = {
    async getLogs(parent: any, args: QueryGetLogsArgs, context: any): Promise<any> {
        try {
            const limit = args.limit || 20;
            const offset = args.offset || 0;
            const logsRaw = await Log.find().limit(limit).skip(offset).sort({ Timestamp: -1 });
            const logs = logsRaw.map((log: any) => {
                return {
                    ...log._doc,
                    timestamp: log.Timestamp.toISOString()
                }
            });
            return logs;
            
        } catch (error: any) {
            logger.error(error.message, 'getLogs');
            throw error;
        }
    }
}

export const mutations: MutationResolvers = {
    
}