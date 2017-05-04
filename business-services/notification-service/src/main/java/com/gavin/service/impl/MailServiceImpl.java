package com.gavin.service.impl;

import com.gavin.model.UserCreatedMailDto;
import com.gavin.service.MailService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

@Service
@RefreshScope
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${mail.from}")
    private String mailFrom;

    private final String mailEncoding = "UTF-8";

    @Retryable(
            value = {MessagingException.class, UnsupportedEncodingException.class, MailException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, maxDelay = 500))
    @Override
    public void sendUserCreatedNotificationMail(UserCreatedMailDto _mailDto) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailFrom, "Spring Cloud Demo");
        helper.setTo(_mailDto.getEmail());
        helper.setSubject("您的帐号创建成功");

        VelocityContext context = new VelocityContext();
        context.put("param", _mailDto);

        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("user_created_notification.vm", mailEncoding, context, stringWriter);
        helper.setText(stringWriter.toString(), true);

        mailSender.send(mimeMessage);
    }

}
