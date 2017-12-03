package com.gavin.business.exception;

import com.gavin.common.exception.CustomException;

/**
 * 邮件发送失败
 */
public class EmailSendException extends CustomException {

    public EmailSendException(Throwable t) {
        super("send email failed.", t);
    }

    @Override
    public String getErrorCode() {
        return "send_email_failed";
    }

}
