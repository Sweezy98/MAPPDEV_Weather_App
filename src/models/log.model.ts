import {Schema, model} from "mongoose";

const logSchema = new Schema({
    Timestamp: {
        type: Date,
        required: true
    },
    level: {
        type: String,
        required: true
    },
    trace: {
        type: String,
        required: true
    },
    message: {
        type: String,
        required: true
    }
});

export default model('Log', logSchema);