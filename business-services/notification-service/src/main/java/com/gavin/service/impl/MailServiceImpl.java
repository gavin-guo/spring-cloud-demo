package com.gavin.service.impl;

import com.gavin.event.UserCreatedEvent;
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

    private final JavaMailSender mailSender;

    private final VelocityEngine velocityEngine;

    @Value("${micro.service.mail.from}")
    private String mailFrom;

    private final String mailEncoding = "UTF-8";

    @Autowired
    public MailServiceImpl(
            JavaMailSender mailSender,
            VelocityEngine velocityEngine) {
        this.mailSender = mailSender;
        this.velocityEngine = velocityEngine;
    }

    @Retryable(
            value = {MessagingException.class, UnsupportedEncodingException.class, MailException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, maxDelay = 500))
    @Override
    public void sendUserCreatedNotificationMail(UserCreatedEvent _event) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailFrom, "Microservice Demo");
        helper.setTo(_event.getEmail());
        helper.setSubject("您在Microservice的帐号创建成功");

        VelocityContext context = new VelocityContext();
        context.put("param", _event);

        StringWriter stringWriter = new StringWriter();
        velocityEngine.mergeTemplate("user_created_notification.vm", mailEncoding, context, stringWriter);
        helper.setText(stringWriter.toString(), true);

        mailSender.send(mimeMessage);
    }

}
