import { QueryResolvers, MutationResolvers } from '../types/resolvers-types';

import Logger from "../../../../helpers/logger";

const logger = new Logger('Resolvers.Hello');

export const queries: QueryResolvers = {
    // hello query to test the graphql api
    async sayHello(parent: any, args: any, context: any) {
        logger.info(`Sayed hello to ${args.name}!`, 'sayHello');
        return `Hello, ${args.name}! This is London calling!`
    }
}

export const mutations: MutationResolvers = {
    
}