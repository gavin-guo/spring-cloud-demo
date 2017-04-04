package com.gavin.service;

import com.gavin.event.UserCreatedEvent;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    /**
     * 发送用户帐号已创建的通知邮件。
     *
     * @param _event
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    void sendUserCreatedNotificationMail(UserCreatedEvent _event) throws MessagingException, UnsupportedEncodingException;

}
