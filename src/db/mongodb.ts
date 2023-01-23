// const mongoose = require("mongoose");
import mongoose from "mongoose";
import Logger from "../helpers/logger";

const logger = new Logger('mongoDB');

export const connect = async () => {
    try {
        await mongoose.connect(`mongodb+srv://${process.env.MONGO_USER as string}:${process.env.MONGO_PASSWORD as string}@${process.env.MONGO_HOST as string}/?authMechanism=DEFAULT`, 
            {dbName: process.env.MONGO_DB as string});
        logger.startup(`Connected to MongoDB!`, 'connect');
    }
    catch (err: any) {
        logger.critical(`Could not connect to MongoDB! Error: ${err.message}`, 'connect');
        throw err
    }
}

export default {connect};