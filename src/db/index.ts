import mongodb from "./mongodb";

export const connect = async () => {
    await mongodb.connect();
}

export default {connect};