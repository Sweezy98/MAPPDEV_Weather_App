import { Resolvers } from '../types/resolvers-types';

import { queries as userQueries, mutations as userMutations } from "./user";
import { queries as weatherQueries, mutations as weatherMutations } from "./weather";
import { queries as helloQueries, mutations as helloMutations } from "./hello";
import { queries as logQueries, mutations as logMutations } from "./log";


// combine all resolvers
export const resolvers: Resolvers = {
    Query: {
        ...userQueries,
        ...weatherQueries,
        ...helloQueries,
        ...logQueries
    },
    
    Mutation: {
        ...userMutations,
        ...weatherMutations,
        ...helloMutations,
        ...logMutations
    }
}

export default resolvers;