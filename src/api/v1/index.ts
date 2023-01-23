import express, { Express } from 'express';

import Logger from '../../helpers/logger';

import { isAuth } from './middleware/auth';

import gql from './graphql';
import verify from './verify';

import session from './models/session.model';

const logger = new Logger('API');

const api: Express = express();

// chech all sessions and delete expired ones
async function checkSessions() {
    try {
        const sessions = await session.find({});

        sessions.forEach(async (session) => {
            if (session.validUntil < new Date()) {
                await session.deleteOne();
                logger.info(`Session ${session._id} deleted due to expiration.`, 'checkSessions');
            }
        });
    }
    catch (err: any) {
        logger.error(err.message, 'checkSessions');
    }
}

//checkSessions();

api.use(isAuth);

api.use('/graphql', gql);
api.use('/verify', verify);

export default api;