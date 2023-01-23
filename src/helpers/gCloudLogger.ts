const {Logging} = require('@google-cloud/logging');

// Your Google Cloud Platform project ID
const projectId = 'mappdev';
 // The name of the log to write to
const logName = 'Sweather';

export enum GCloudLoggerSeverity {
    DEFAULT = 'DEFAULT',
    DEBUG = 'DEBUG',
    INFO = 'INFO',
    NOTICE = 'NOTICE',
    WARNING = 'WARNING',
    ERROR = 'ERROR',
    CRITICAL = 'CRITICAL',
    ALERT = 'ALERT',
    EMERGENCY = 'EMERGENCY',
}

export default async function gCloudLogger (severity: GCloudLoggerSeverity, message: string) {
    // Creates a client
    const logging = new Logging({projectId});
    // Selects the log to write to
    const log = logging.log(logName);

    // The metadata associated with the entry
    const metadata = {
        resource: {type: 'global'},
        // See: https://cloud.google.com/logging/docs/reference/v2/rest/v2/LogEntry#logseverity
        severity
    };

    const entry = log.entry(metadata, message);

    await log.write(entry);
}