package com.gavin.exception;

/**
 * 邮件发送失败
 */
public class EmailSendingException extends RuntimeException {

    public EmailSendingException() {
    }

    public EmailSendingException(String message) {
        super(message);
    }

}
