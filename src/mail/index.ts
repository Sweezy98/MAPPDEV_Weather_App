import nodemailer from "nodemailer";
import Mail, { Address } from "nodemailer/lib/mailer";
import internal from "stream";

import Logger from "../helpers/logger";

const logger = new Logger('mailer');

interface MailOptions {
    to: string | Address | (string | Address)[] | undefined,
    subject: string | undefined, 
    text?: string | Buffer | internal.Readable | Mail.AttachmentLike | undefined, 
    html?: string | Buffer | internal.Readable | Mail.AttachmentLike | undefined,
    headers?: Mail.Headers | undefined
}


export async function sendMail(Options: MailOptions){
    const transporter = nodemailer.createTransport({
        host: process.env.MAIL_HOST,
        port: 587,
        secure: false,
        requireTLS: true,
        auth: {
            user: process.env.MAIL_USERNAME,
            pass: process.env.MAIL_PASSWORD,
        },
        logger: false,
    });

    const info = await transporter.sendMail({
        from: 'no-reply@sweather.at',
        to: Options.to,
        subject: Options.subject,
        text: Options.text,
        html: Options.html,
        headers: Options.headers
    });

    logger.info(`Email ${Options.subject} sent to ${Options.to}!`, 'sendMail');
}

