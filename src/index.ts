import express, { Express } from 'express';
import dotenv from 'dotenv';

dotenv.config();

import databases from "./db";

import api_v1 from "./api/v1";
import Logger from './helpers/logger';
import path from 'path';

const port = process.env.PORT || 8080;
const server: Express = express();

const logger = new Logger('Server');


//* route to api version 1
server.use('/api/v1', api_v1);

// serve index.html for /admin and subroutes
server.get('/admin*', (req, res) => {
    res.sendFile('index.html', { root: path.join(__dirname, '/admin') });
});

// static files /public
server.use('/public', express.static(path.join(__dirname, '/admin'), { index: false }));


const startServer = async () => {
    try {
        await databases.connect();

        server.listen(port, () => {
            if (process.env.NODE_ENV === 'production') {
                logger.startup(`Server is running in production environment`, 'startServer');
            }
            else {
                logger.startup(`Server is running in development environment`, 'startServer');
                logger.startup(`Server is listening on http://localhost:${port}`, 'startServer');
            }
        });
    }
    catch (err: any) {
        logger.critical(`Could not start server! Error: ${err.message}`, 'startServer');
        throw err
    }
}

startServer();

process.on('SIGTERM', () => {
    logger.startup('Server is shutting down', 'SIGTERM');
    process.exit(0);
});

process.on('SIGINT', () => {
    logger.startup('Server is shutting down', 'SIGINT');
    process.exit(0);
});