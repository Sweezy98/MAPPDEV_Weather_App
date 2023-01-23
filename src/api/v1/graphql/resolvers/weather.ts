import axios from "axios";

import { QueryResolvers, MutationResolvers, WeatherData, WeatherDataForecast } from '../types/resolvers-types';

import Logger from "../../../../helpers/logger";

import weatherCache from "../../models/weatherCache.model";
import Favourite from "../../models/favourite.model";

const logger = new Logger('Resolvers.Weather');

enum Clothing {
    NONE = "NONE",
    SHORTS = "SHORTS",
    LONGS = "LONGS",
    TSHIRT = "TSHIRT",
    SWEATER = "SWEATER",
    JACKET = "JACKET",
    RAINCOAT = "RAINCOAT",
    UMBRELLA = "UMBRELLA",
    WINTERJACKET = "WINTERJACKET"
}

enum Icons {
    CLOUD = "cloud",
    CLOUD_SUN = "cloud_sun",
    FOG = "fog",
    RAIN = "rain",
    SNOW = "snow",
    STORM = "storm",
    SUN = "sun"
}

// code 200-232: storm
// code 300-321: rain
// code 500-531: rain
// code 600-622: snow
// code 701-781: fog
// code 800: sun
// code 801-804: cloud

const iconMapping: any = {
    200: Icons.STORM,
    201: Icons.STORM,
    202: Icons.STORM,
    210: Icons.STORM,
    211: Icons.STORM,
    212: Icons.STORM,
    221: Icons.STORM,
    230: Icons.STORM,
    231: Icons.STORM,
    232: Icons.STORM,
    300: Icons.RAIN,
    301: Icons.RAIN,
    302: Icons.RAIN,
    310: Icons.RAIN,
    311: Icons.RAIN,
    312: Icons.RAIN,
    313: Icons.RAIN,
    314: Icons.RAIN,
    321: Icons.RAIN,
    500: Icons.RAIN,
    501: Icons.RAIN,
    502: Icons.RAIN,
    503: Icons.RAIN,
    504: Icons.RAIN,
    511: Icons.RAIN,
    520: Icons.RAIN,
    521: Icons.RAIN,
    522: Icons.RAIN,
    531: Icons.RAIN,
    600: Icons.SNOW,
    601: Icons.SNOW,
    602: Icons.SNOW,
    611: Icons.SNOW,
    612: Icons.SNOW,
    613: Icons.SNOW,
    615: Icons.SNOW,
    616: Icons.SNOW,
    620: Icons.SNOW,
    621: Icons.SNOW,
    622: Icons.SNOW,
    701: Icons.FOG,
    711: Icons.FOG,
    721: Icons.FOG,
    731: Icons.FOG,
    741: Icons.FOG,
    751: Icons.FOG,
    761: Icons.FOG,
    762: Icons.FOG,
    771: Icons.FOG,
    781: Icons.FOG,
    800: Icons.SUN,
    801: Icons.CLOUD_SUN,
    802: Icons.CLOUD,
    803: Icons.CLOUD,
    804: Icons.CLOUD,
};


const transformWeatherData = (current: any, forecast: any): WeatherData => {
    // get name of city and country
    const { name, sys: { country } } = current;

    // get needed data from current
    // description, id, temps: {temp as cur, temp_min as min, temp_max as max, feels_like as feel}, humidity, precipitation: max rain or snow, visibility, pressure, wind: {speed, deg}
    const { weather, main, wind, visibility, rain, snow} = current;
    const { description, id } = weather[0];
    const { temp, temp_min, temp_max, feels_like, pressure, humidity } = main;
    const { speed, deg } = wind;
    var rainVal = 0;
    if (rain != undefined) {
        if(rain["3h"] != undefined) {
            rainVal = rain["3h"];
        } else if (rain["1h"] != undefined) {
            rainVal = rain["1h"];
        }
    }
    var snowVal = 0;
    if (snow != undefined) {
        if(snow["3h"] != undefined) {
            snowVal = snow["3h"];
        } else if (snow["1h"] != undefined) {
            snowVal = snow["1h"];
        }
    }
    const precipitation = Math.max(rainVal, snowVal);

    const currentDay = {
        // date is current date, format: 01.05.2020
        date: new Date(),
        description,
        id,
        temps: {
            cur: temp,
            min: temp_min,
            max: temp_max,
            feel: feels_like
        },
        humidity,
        precipitation,
        visibility,
        pressure,
        wind: {
            speed,
            deg
        }
    };


    // forecast.list contains an array with a forecast for 5 days, every 3 hours
    const forecastList = forecast.list;
    // group data by day
    const forecastListByDay = forecastList.reduce((acc: any, cur: any) => {
        const date = cur.dt_txt.split(" ")[0];
        if (!acc[date]) {
            acc[date] = [];
        }
        acc[date].push(cur);
        return acc;
    }, {});

    // days contains an array with a forecast for 5 days
    // combine data for each day
    // min temp, max temp, average feels_like, average humidity, average rain, average snow, average visibility, average pressure
    // max wind speed, direction and from max entry
    // description and id from first entry
    const forecastDays = Object.keys(forecastListByDay).map((key: any) => {
        const day = forecastListByDay[key];
        // get date from first entry
        const date = day[0].dt_txt;
        const description = day[0].weather[0].description;
        const id = day[0].weather[0].id;
        // temp_min is min of all entries
        const temp_min = day.reduce((acc: any, cur: any) => {
            if (acc === undefined) {
                return cur.main.temp_min;
            }
            return Math.min(acc, cur.main.temp_min);
        }, undefined);
        // temp_max is max of all entries
        const temp_max = day.reduce((acc: any, cur: any) => {
            if (acc === undefined) {
                return cur.main.temp_max;
            }
            return Math.max(acc, cur.main.temp_max);
        }, undefined);
        const feels_like = day.reduce((acc: any, cur: any) => acc + cur.main.feels_like, 0) / day.length;
        const humidity = day.reduce((acc: any, cur: any) => acc + cur.main.humidity, 0) / day.length;
        const rain = day.reduce((acc: any, cur: any) => acc + (cur.rain ? cur.rain["3h"] : 0), 0) / day.length;
        const snow = day.reduce((acc: any, cur: any) => acc + (cur.snow ? cur.snow["3h"] : 0), 0) / day.length;
        // precipitation max rain and snow
        const precipitation = Math.max(rain, snow);
        const visibility = day.reduce((acc: any, cur: any) => acc + cur.visibility, 0) / day.length;
        const pressure = day.reduce((acc: any, cur: any) => acc + cur.main.pressure, 0) / day.length;
        const wind = day.reduce((acc: any, cur: any) => {
            if (cur.wind.speed > acc.speed) {
                return {
                    speed: cur.wind.speed,
                    deg: cur.wind.deg
                };
            }
            return acc;
        }, { speed: 0, deg: 0 });
        return {
            date,
            description,
            id,
            temps: {
                min: temp_min,
                max: temp_max,
                feel: feels_like
            },
            humidity,
            precipitation,
            visibility,
            pressure,
            wind
        };
    });

    // if first day is today, remove it
    if (forecastDays[0].date.split(" ")[0] === new Date().toISOString().split("T")[0]) {
        forecastDays.shift();
    }
    
    // if forecast is for more than 4 days, remove last days
    if (forecastDays.length > 4) {
        forecastDays.splice(4);
    }

    // if forecast is for less than 4 days, add empty days
    while (forecastDays.length < 4) {
        forecastDays.push({
            date: new Date(),
            description: "",
            id: 0,
            temps: {
                min: 0,
                max: 0,
                feel: 0
            },
            humidity: 0,
            precipitation: 0,
            visibility: 0,
            pressure: 0,
            wind: {
                speed: 0,
                deg: 0
            }
        });
    }

    // combine current day and forecast days
    const days = [currentDay, ...forecastDays];

    // convert data to WeatherDataForecast
    const weatherDataForecast: WeatherDataForecast[] = days.map((day: any) => {
        const dayForecast: WeatherDataForecast = {
            // format: 2020-05-01 12:00:00 -> 01.05.2020
            date: new Date(day.date).toLocaleString('de-DE', { day: '2-digit', month: '2-digit', year: 'numeric' }),
            weekday: {
                long: new Date(day.date).toLocaleString('en-GB', { weekday: 'long' }),
                short: new Date(day.date).toLocaleString('en-GB', { weekday: 'short' })
            },
            description: day.description,
            temps: {
                cur: {
                    c: day.temps.cur ? Math.round(day.temps.cur) : null,
                    f: day.temps.cur ? Math.round(day.temps.cur * 9/5 + 32) : null
                },
                min: {
                    c: Math.round(day.temps.min),
                    f: Math.round(day.temps.min * 9/5 + 32)
                },
                max: {
                    c: Math.round(day.temps.max),
                    f: Math.round(day.temps.max * 9/5 + 32)
                },
                feel: {
                    c: Math.round(day.temps.feel),
                    f: Math.round(day.temps.feel * 9/5 + 32)
                }
            },
            details: {
                humidity: day.humidity,
                // precipitation is either rain or snow, if both are present, take the higher value
                precipitation: day.precipitation,
                visibility: day.visibility,
                wind: day.wind,
                pressure: day.pressure
            },
            code: day.id,
            icon: iconMapping[day.id],
        }
        return dayForecast;
    });

    const weatherData: WeatherData = {
        name: name + ', ' + country,
        weather: weatherDataForecast
    }

    return weatherData;
}

const fetchFromAPI = async (lat: number, lon: number) => {
    var urlCurrent = process.env.OPENWEATHERMAP_API_URL + `?lat=${lat}&lon=${lon}&appid=${process.env.OPENWEATHERMAP_API_KEY}&lang=en&units=metric`;
    var urlForecast = process.env.OPENWEATHERMAP_API_URL_FORECAST + `?lat=${lat}&lon=${lon}&appid=${process.env.OPENWEATHERMAP_API_KEY}&lang=en&units=metric`;

        try {
            const responseCurrent = await axios.get(urlCurrent);
            const currentData = responseCurrent.data;
            const responseForecast = await axios.get(urlForecast);
            const forecastData = responseForecast.data;
            return transformWeatherData(currentData, forecastData);
        }
        catch (error: any) {
            logger.error(error.message, 'fetchFromAPI');
            throw error;
        }
}

//maxTTL 30 minutes
const maxTTL = 30 * 60 * 1000;

enum CacheStatus {
    CACHED,
    NOT_CACHED,
    EXPIRED
}


// cut lat and lon to 2 decimal places, to avoid duplicate entries
const latLonCrop = (l: number): number => {
    const str = l.toString().split('.');
    return parseFloat(str[0] + '.' + str[1].substring(0, 2));
}


// check if weather data is cached
// return CacheStatus and weather data if cached

const fetcheFromCache = async (lat: number, lon: number): Promise<{status: CacheStatus, data: WeatherData | null, cache: string | null}> => {
    const cache = await weatherCache.findOne({ lat: latLonCrop(lat), lon: latLonCrop(lon) });
    if (cache) {
        //check if cache is still valid
        if (cache.timestamp.getTime() + maxTTL > new Date().getTime()) {
            return { status: CacheStatus.CACHED, data: cache.data, cache: cache.id };
        } else {
            return { status: CacheStatus.EXPIRED, data: null, cache: cache.id };
        }
    } else {
        return { status: CacheStatus.NOT_CACHED, data: null, cache: null };
    }
}

const updateCache = async (id: string, data: WeatherData) => {
    const cache = await weatherCache.findById(id);
    if (cache) {
        cache.data = data;
        cache.timestamp = new Date();
        await cache.save();
    }
}

const createCache = async (lat: number, lon: number, data: WeatherData) => {
    const cache = new weatherCache({
        lat: latLonCrop(lat),
        lon: latLonCrop(lon),
        data: data,
        timestamp: new Date()
    });
    await cache.save();
}


export const queries: QueryResolvers = {
    async getWeatherData(parent: any, args: any, context: any): Promise<any> {
        var data: WeatherData = {};
        // try to fetch from cache
        const cache = await fetcheFromCache(args.lat, args.lon);
        if (cache.status === CacheStatus.CACHED) {
            logger.info(`Weather data for ${args.lat}, ${args.lon} fetched from cache!`, 'getWeatherData');
            data = cache.data!!;
        } else {
            if (cache.status === CacheStatus.EXPIRED) {
                logger.info(`Weather data for ${args.lat}, ${args.lon} expired in cache!`, 'getWeatherData');
            }
            if (cache.status === CacheStatus.NOT_CACHED) {
                logger.info(`Weather data for ${args.lat}, ${args.lon} not cached!`, 'getWeatherData');
            }
            // fetch from API
            data = await fetchFromAPI(args.lat, args.lon);
            logger.info(`Weather data for ${args.lat}, ${args.lon} fetched from OpenWeatherMap API!`, 'getWeatherData');
            // save to cache
            if (cache.status === CacheStatus.EXPIRED) {
                await updateCache(cache.cache!!, data);
                logger.info(`Weather data for ${args.lat}, ${args.lon} updated in cache!`, 'getWeatherData');
            } else {
                await createCache(args.lat, args.lon, data);
                logger.info(`Weather data for ${args.lat}, ${args.lon} created in cache!`, 'getWeatherData');
            }
        }

        if (context.isAuth) {
            // check if location is favorited by current user
            const favourite = await Favourite.findOne({ user: context.userId, name: data.name });
            if (favourite) {
                data.isFavourite = true;
                data.favouriteId = favourite.id;
            } else {
                data.isFavourite = false;
                data.favouriteId = null;
            }
        } else {
            data.isFavourite = false;
            data.favouriteId = null;
        }

        return data;
    }
}

export const mutations: MutationResolvers = {
    
}