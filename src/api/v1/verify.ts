import express, { Express } from 'express';

import User from "./models/user.model";
import Logger from '../../helpers/logger';
import { sendMail } from '../../mail';

const logger = new Logger('verifyUser');

const verify: Express = express();

verify.get('/', async (req, res, next) => {
    if(!req.query.id) {
        res.status(400).send('Invalid verification link!').end();
        logger.warning('Invalid verification link!', 'getRoute');
    }
    else {
        const user = await User.findById(req.query.id);
        if (user != null) {
            if (user.verified === true) {
                res.status(200).send('Your email is already verified!').end();
                logger.info('User already verified!', 'getRoute');
            }
            else {
                user.verified = true;
                await user.save();
                res.status(200).send('Thank you for verifying your email!').end();
                logger.info('User verified!', 'getRoute');
                sendMail({
                    to: user.email,
                    subject: 'Email verified!',
                    html: `<h1>Thank you for verifying your email!</h1>
                    <p>You can now login to the Sweather App!</p>`
                });
            }
        }
        else {
            res.status(404).send('Invalid verification link!').end();
            logger.warning('Invalid verification link!', 'getRoute');
        }
    }
});

export default verify