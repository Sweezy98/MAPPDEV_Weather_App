import {Schema, model} from "mongoose";

const weatherCacheSchema = new Schema({
    timestamp: {
        type: Date,
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
    data: {
        type: Object,
        required: true
    }
});

export default model('WeatherCache', weatherCacheSchema);