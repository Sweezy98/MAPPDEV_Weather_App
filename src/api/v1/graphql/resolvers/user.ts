import { ApolloError } from 'apollo-server-express';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';

import { QueryResolvers, MutationResolvers } from '../types/resolvers-types';

import Logger from '../../../../helpers/logger';
import { sendMail } from '../../../../mail';

import User from "../../models/user.model";
import Session from "../../models/session.model";
import Favourite from "../../models/favourite.model";

const logger = new Logger('Resolvers.User');

const transformUser = (user: any) => {
    delete user._doc.password;
    return { ...user._doc, _id: user.id }
}

export const queries: QueryResolvers = {
    async login(parent: any, args: any, context: any) {
        try {
            const user = await User.findOne({email: args.email});
            if (!user) {
                throw new ApolloError('Wrong email or password!', '401 Unauthorized');
            };
            
            if(user.verified === false) {
                throw new ApolloError('Please verify your email!', '401 Unauthorized');
            };

            const isEqual = await bcrypt.compare(args.password, user.password);
            if (!isEqual) {
                throw new ApolloError('Wrong email or password!', '401 Unauthorized');
            };

            const accessToken = jwt.sign({
                userID: user.id, 
                name: user.name,
                email: user.email,
                issued_token_type: "Bearer",
            }, process.env.JWT_privateKey as string, {
                expiresIn: '15m'
            });

            // generate random refresh token UUID
            const refreshTokenUUID = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);
            
            // create session
            const session = new Session({
                user: user.id,
                lastRefreshToken: refreshTokenUUID,
                // validUntil now in 30 days
                validUntil: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000)
            });
            await session.save();

            const refreshToken = jwt.sign({
                userID: user.id, 
                name: user.name,
                email: user.email,
                UUID: refreshTokenUUID,
                session: session.id,
                issued_token_type: "Refresh",
            }, process.env.JWT_privateKey as string, {
                expiresIn: '1d'
            });

            const tokens = {accessToken, refreshToken};


            logger.info(`User ${user.name} logged in successfully! Session ${session.id} created.`, 'login');


            return { userId: user.id,
                     name: user.name,
                     email: user.email,
                     verified: user.verified,
                     session: session.id,
                     tokens }
        }
        catch (err: any) {
            logger.error(err.message, 'login');
            throw err
        };
    },
    async renewToken(parent: any, args: any, context: any) {
        try {
            const decodedRefreshToken: any = jwt.verify(args.refreshToken, process.env.JWT_privateKey as string);
            if(!decodedRefreshToken) {
                logger.warning(`Invalid Token! Data: ${args.refreshToken}`, 'renewToken');
                throw new ApolloError('Invalid Token', '401 Unauthorized');
            };

            if (decodedRefreshToken.issued_token_type !== "Refresh") {
                logger.warning(`Token is not a refresh token! Token: ${decodedRefreshToken}`, 'renewToken');
                throw new ApolloError('Invalid Token type!', '401 Unauthorized');
            };

            const session = await Session.findById(decodedRefreshToken.session);
            const user = await User.findById(decodedRefreshToken.userID);
            if(!session) {
                logger.warning(`There is no session for this token! User: ${user}, Session: ${decodedRefreshToken.session}`, 'renewToken');
                throw new ApolloError('Invalid Token', '401 Unauthorized');
            };

            if(session.user.toString() !== decodedRefreshToken.userID) {
                logger.warning(`User ID in token does not match session user ID! User: ${user}, Session: ${decodedRefreshToken.session}`, 'renewToken');
                throw new ApolloError('Invalid Token', '401 Unauthorized');
            };

            if(session.lastRefreshToken !== decodedRefreshToken.UUID) {
                logger.alert(`Refresh token UUID does not match session last refresh token UUID!  User: ${user}, Session: ${decodedRefreshToken.session}`, 'renewToken');
                await session.delete();
                logger.alert(`Session ${session.id} deleted due to compromised refresh token. User: ${user}`,'renewToken');
                throw new ApolloError('Invalid Token', '401 Unauthorized');
            };

            // check if token is expired
            if(decodedRefreshToken.exp < Date.now() / 1000) {
                logger.info('Token expired!', 'renewToken');
                // delete session
                await session.delete();
                logger.info(`Session ${session.id} deleted due to expired refresh token. User: ${user}`,'renewToken');
                throw new ApolloError('Token expired', '401 Unauthorized');
            };

            const accessToken = jwt.sign({
                userID: decodedRefreshToken.userID, 
                name: decodedRefreshToken.name,
                email: decodedRefreshToken.email,
                issued_token_type: "Bearer",
            }, process.env.JWT_privateKey as string, {
                expiresIn: '15m'
            });

            // generate random refresh token UUID
            const refreshTokenUUID = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15);

            const refreshToken = jwt.sign({
                userID: decodedRefreshToken.userID, 
                name: decodedRefreshToken.name,
                email: decodedRefreshToken.email,
                UUID: refreshTokenUUID,
                session: decodedRefreshToken.session,
                issued_token_type: "Refresh",
            }, process.env.JWT_privateKey as string, {
                expiresIn: '30d'
            });

            // update session
            session.lastRefreshToken = refreshTokenUUID;
            session.validUntil = new Date(Date.now() + 30 * 24 * 60 * 60 * 1000);
            await session.save();

            const tokens = {accessToken, refreshToken};

            logger.info(`User ${decodedRefreshToken.name} renewed token successfully! Session ${session.id} updated!`, 'renewToken');

            return tokens;
        }
        catch (err: any) {
            logger.error(err.message, 'renewToken');
            throw err
        }
    },
    async forgotPassword(parent: any, args: any, context: any) {
        try {
            const user = await User.findOne({email: args.email});
            if (!user) {
                logger.info(`User with email ${args.email} does not exist!`, 'forgotPassword');
                return true;
            };
            
            if(user.verified === false) {
                throw new ApolloError('Please verify your email!', '401 Unauthorized');
            };

            // create new random password with 8 characters and numbers
            const newPassword = Math.random().toString(36).slice(-8);
            const passwordHash = await bcrypt.hash(newPassword, 12);

            // set new password
            user.password = passwordHash;
            await user.save();

            logger.info(`User ${user.name} successfully reset password!`, 'forgotPassword');

            // send email with new password
            sendMail({
                to: user.email,
                subject: 'Sweather - Password Reset',
                html: `<h1>Sweather - Password Reset</h1>
                <p>Your new password is: ${newPassword}</p>
                <p>Please change your password after you login!</p>`
            });

            return true;
        }
        catch (err: any) {
            logger.error(err.message, 'forgotPassword');
            throw err
        }
    },
    async logout(parent: any, args: any, context: any) {
        try {
            const session = await Session.findById(args.session).populate('user');
            if(!session) {
                throw new ApolloError('Invalid Session', '401 Unauthorized');
            }

            await session.delete();

            const user = await User.findById(session.user.id);

            logger.info(`User ${user?.name} logged out successfully! Session ${session.id} deleted!`, 'logout');

            return true;
        }
        catch (err: any) {
            logger.error(err.message, 'logout');
            throw err
        }
    },
   async userFavourites(parent: any, args: any, context: any): Promise<any> {
        try {
            if (!context.isAuth) {
                throw new ApolloError('Not authenticated!', '401 Unauthorized');
            };

            // get all favourites for user
            const favourites = await Favourite.find({user: context.userId});

            return favourites;
        }
        catch (err: any) {
            logger.error(err.message, 'userFavourites');
            throw err
        }
   }
}

export const mutations: MutationResolvers = {
    async createUser(parent: any, args: any, context: any) {
        try {
            const checkExistsAlready = await User.findOne({email: args.user.email})

            if (!checkExistsAlready){
                const passwordHash = await bcrypt.hash(args.user.password, 12);

                const user = new User({
                    ...args.user,
                    verified: false,
                    password: passwordHash
                });
                
                await user.save();

                sendMail({
                    to: user.email,
                    subject: 'Welcome to Sweather! Please verify your email!',
                    html: `<h1>Welcome to Sweather!</h1>
                    <p>Please verify your email by clicking the link below:</p>
                    <a href="https://${process.env.DOMAIN}/api/v1/verify?id=${user.id}">Verify Email</a>`
                });

                logger.info(`User ${args.user.name} created!`, 'createUser');

                return transformUser(user);
            }
            else {
                logger.warning(`User ${args.user.name} already exists!`, 'createUser');
                throw new ApolloError('User already exists!', 'USER_EXISTS');
            };
        }
        catch(err: any) {
            logger.error(err.message, 'createUser');
            throw err
        };
    },
    async changePassword(parent: any, args: any, context: any) {
        try {
            if (!context.isAuth) {
                throw new ApolloError('Not authenticated!', '401 Unauthorized');
            };
            
            const user = await User.findById(context.userId);
            if (!user) {
                throw new ApolloError('User not found!', '404 Not Found');
            };

            const isEqual = await bcrypt.compare(args.oldPassword, user.password);
            if (!isEqual) {
                throw new ApolloError('Wrong password!', '401 Unauthorized');
            };

            const isSame = await bcrypt.compare(args.newPassword, user.password);
            if (isSame) {
                throw new ApolloError('Your new password cannot be the same as your old password!', '400 Bad Request');
            };

            const passwordHash = await bcrypt.hash(args.newPassword, 12);
            user.password = passwordHash;
            await user.save();

            logger.info(`User ${user.name} changed password!`, 'changePassword');

            sendMail({
                to: user.email,
                subject: 'Sweather - Password Changed',
                html: `<h1>Sweather - Password Changed</h1>
                <p>Your password has been changed!</p>
                <p>If you did not change your password, please contact us immediately!</p>`
            });

            return true;
        }
        catch(err: any) {
            logger.error(err.message, 'changePassword');
            throw err
        }
    },
    async addFavourite(parent: any, args: any, context: any): Promise<any> {
        try {
            if (!context.isAuth) {
                throw new ApolloError('Not authenticated!', '401 Unauthorized');
            };
            
            // check if favourite already exists
            const favourite = await Favourite.findOne({user: context.userId, lat: args.favourite.lat, lon: args.favourite.lon});
            if (favourite) {
                throw new ApolloError('Favourite already exists!', '400 Bad Request');
            };

            const user = await User.findById(context.userId);

            const newFavourite = new Favourite({
                user: user?.id,
                name: args.favourite.name,
                lat: args.favourite.lat,
                lon: args.favourite.lon
            });

            await newFavourite.save();

            logger.info(`User ${user?.name} added favourite ${args.favourite.name}!`, 'addFavourite');

            // get all favourites for user
            const favourites = await Favourite.find({user: context.userId});

            return favourites;
        }
        catch(err: any) {
            logger.error(err.message, 'addFavourite');
            throw err
        }
    },
    async removeFavourite(parent: any, args: any, context: any): Promise<any> {
        try {
            if (!context.isAuth) {
                throw new ApolloError('Not authenticated!', '401 Unauthorized');
            };
            
            const favourite = await Favourite.findById(args.id);
            if (!favourite) {
                throw new ApolloError('Favourite not found!', '404 Not Found');
            };

            const user = await User.findById(context.userId);

            await favourite.delete();

            logger.info(`User ${user?.name} removed favourite ${favourite.name}!`, 'removeFavorite');

            // get all favourites for user
            const favourites = await Favourite.find({user: context.userId});

            return favourites;
        }
        catch(err: any) {
            logger.error(err.message, 'removeFavorite');
            throw err
        }
    },
}