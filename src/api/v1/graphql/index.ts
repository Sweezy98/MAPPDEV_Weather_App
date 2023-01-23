import express, { Express, Request } from 'express';
import { ApolloServer } from 'apollo-server-express';
import { readdirSync, readFileSync } from 'fs';

import Logger from '../../../helpers/logger';

import resolvers from './resolvers';

const logger = new Logger('ApolloServer');

const app: Express = express();

const schemaFiles = readdirSync(__dirname + '/schema').filter(dirent => dirent.endsWith('.graphql'));

const typeDefs = schemaFiles.map(file => readFileSync(__dirname + `/schema/${file}`, 'utf8'));


const apollo = new ApolloServer({ typeDefs, resolvers, nodeEnv: "", introspection: true, context: ({ req }:{ req: Request }) => {
    const isAuth = req.isAuth;
    const token = req.token || null;
    const userId = req.userId || null;
    const session = req.session || null;

    return { isAuth, token, userId, session };
}});

const startApollo = async () => {
    await apollo.start();
    logger.startup('Apollo Server started!', 'startApollo');
    apollo.applyMiddleware({ app, path: '/'});
    logger.startup('Apollo Server Middleware applied to Express!', 'startApollo');
}

startApollo();

export default app;