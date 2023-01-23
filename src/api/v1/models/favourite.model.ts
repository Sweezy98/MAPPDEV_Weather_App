import {Schema, model} from "mongoose";

import User from "./user.model";

const favouriteSchema = new Schema({
    // user as a reference to the user model
    user: {
        type: Schema.Types.ObjectId,
        ref: User,
        required: true
    },
    name: {
        type: String,
        required: true
    },
    lat: {
        type: Number,
        required: true
    },
    lon: {
        type: Number,
        required: true
    },
});

export default model('Favourite', favouriteSchema);