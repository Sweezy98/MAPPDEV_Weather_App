import gCloudLogger, { GCloudLoggerSeverity } from './gCloudLogger';

import Log from '../models/log.model'

enum LogLevels {
    INFO = 'INFO',
    ERROR = 'ERROR',
    DEBUG = 'DEBUG',
    WARNING = 'WARNING',
    ALERT = 'ALERT',
    CRITICAL = 'CRITICAL',
    STARTUP = 'STARTUP',
    DEFAULT = 'DEFAULT'
}

export default class Logger {
    private instance: string;

    constructor(instance: string) {
        this.instance = instance;
    }

    private async logToDB(level: LogLevels, message: string, trace: string) {
        const log = new Log({
            Timestamp: new Date(),
            level,
            trace,
            message
        });
        await log.save();
    }

    private logToConsole(level: LogLevels, message: string, trace: string) {
        switch (level) {
            case LogLevels.INFO:
                // message color: green
                console.info(`\x1b[32m%s\x1b[0m`, `😀[INFO]: ${message} | ${trace}`);
                break;
            case LogLevels.ERROR:
                // message color: red
                console.error(`\x1b[31m%s\x1b[0m`, `😮[ERROR]: ${message} | ${trace}`);
                break;
            case LogLevels.DEBUG:
                // message color: orange
                console.debug(`\x1b[33m%s\x1b[0m`, `🐞[DEBUG]: ${message} | ${trace}`);
                break;
            case LogLevels.WARNING:
                // message color: yellow
                console.warn(`\x1b[33m%s\x1b[0m`, `⚠️[WARNING]: ${message} | ${trace}`);
                break;
            case LogLevels.ALERT:
                // message color: purple
                console.log(`\x1b[35m%s\x1b[0m`, `🚨[ALERT]: ${message} | ${trace}`);
                break;
            case LogLevels.CRITICAL:
                // message color: red
                console.error(`\x1b[31m%s\x1b[0m`, `🔥[CRITICAL]: ${message} | ${trace}`);
                break;
            case LogLevels.STARTUP:
                // message color: blue
                console.log(`\x1b[34m%s\x1b[0m`, `🚀[STARTUP]: ${message} | ${trace}`);
                break;
            case LogLevels.DEFAULT:
                // message color: white
                console.log(`\x1b[37m%s\x1b[0m`, `⚪[DEFAULT]: ${message} | ${trace}`);
                break;
        }
    }

    private async logToGCloud(level: LogLevels, message: string, trace: string) {
        switch (level) {
            case LogLevels.INFO:
                await gCloudLogger(GCloudLoggerSeverity.INFO, `${message} | ${trace}`);
                break;
            case LogLevels.ERROR:
                await gCloudLogger(GCloudLoggerSeverity.ERROR, `${message} | ${trace}`);
                break;
            case LogLevels.DEBUG:
                await gCloudLogger(GCloudLoggerSeverity.DEBUG, `${message} | ${trace}`);
                break;
            case LogLevels.WARNING:
                await gCloudLogger(GCloudLoggerSeverity.WARNING, `${message} | ${trace}`);
                break;
            case LogLevels.ALERT:
                await gCloudLogger(GCloudLoggerSeverity.ALERT, `${message} | ${trace}`);
                break;
            case LogLevels.CRITICAL:
                await gCloudLogger(GCloudLoggerSeverity.CRITICAL, `${message} | ${trace}`);
                break;
            case LogLevels.STARTUP:
                await gCloudLogger(GCloudLoggerSeverity.INFO, `${message} | ${trace}`);
                break;
            case LogLevels.DEFAULT:
                await gCloudLogger(GCloudLoggerSeverity.DEFAULT, `${message} | ${trace}`);
                break;
        }
    }

    private async log(level: LogLevels, message: string, trace?: string) {
        if (trace) {
            trace = this.instance + '.' + trace;
        } else {
            trace = this.instance;
        }
        if(process.env.NODE_ENV === 'production') {
            await this.logToDB(level, message, trace);
            await this.logToGCloud(level, message, trace);
        } else {
            this.logToConsole(level, message, trace);
        }
    }

    public info(message: string, trace?: string) {
        this.log(LogLevels.INFO, message, trace);
    }

    public error(message: string, trace?: string) {
        this.log(LogLevels.ERROR, message, trace);
    }

    public debug(message: string, trace?: string) {
        this.log(LogLevels.DEBUG, message, trace);
    }

    public warning(message: string, trace?: string) {
        this.log(LogLevels.WARNING, message, trace);
    }

    public alert(message: string, trace?: string) {
        this.log(LogLevels.ALERT, message, trace);
    }

    public critical(message: string, trace?: string) {
        this.log(LogLevels.CRITICAL, message, trace);
    }

    public startup(message: string, trace?: string) {
        this.log(LogLevels.STARTUP, message, trace);
    }

    public default(message: string, trace?: string) {
        this.log(LogLevels.DEFAULT, message, trace);
    }
}