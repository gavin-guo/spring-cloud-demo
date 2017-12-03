package com.gavin.business.service;

import com.gavin.business.dto.UserCreatedMailDto;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    /**
     * 发送用户帐号已创建的通知邮件。
     *
     * @param _mailDto
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    void sendUserCreatedNotificationMail(UserCreatedMailDto _mailDto) throws MessagingException, IOException, TemplateException;

}
