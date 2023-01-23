import {Schema, model} from "mongoose";

import User from "./user.model";

const sessionSchema = new Schema({
    // user as a reference to the user model
    user: {
        type: Schema.Types.ObjectId,
        ref: User,
        required: true
    },
    lastRefreshToken: {
        type: String,
        required: true
    },
    validUntil: {
        type: Date,
        required: true
    }
});

export default model('Session', sessionSchema);