import jwt from 'jsonwebtoken';
import { Request, Response, NextFunction } from 'express';

import Logger from '../../../helpers/logger';

const logger = new Logger('Auth Middleware');

declare global {
    namespace Express {
        interface Request {
            isAuth: boolean;
            token: string;
            userId: string;
            session: string;
        }
    }
}

export const isAuth = (req: Request, res: Response, next: NextFunction) => {
    const authHeader = req.get('Authorization');
    if (!authHeader) {
        req.isAuth = false;
        return next();
    };

    const token = authHeader.split(' ')[1];
    if (!token || token === '') {
        req.isAuth = false;
        return next();
    };

    let decodedToken: any;
    try {
        decodedToken = jwt.verify(token, process.env.JWT_privateKey as string);
    }
    catch (err) {
        req.isAuth = false;
        return next();
    };

    if (!decodedToken) {
        req.isAuth = false;
        return next();
    }

    if (decodedToken.issued_token_type === "Bearer") {
        req.isAuth = true;
        req.token = decodedToken;
        req.userId = decodedToken.userID;
        req.session = decodedToken.session;
        next();
    }
    else {
        req.isAuth = false;
        return next();
    }

};