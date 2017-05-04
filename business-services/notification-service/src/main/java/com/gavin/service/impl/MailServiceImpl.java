package com.gavin.service.impl;

import com.gavin.model.UserCreatedMailDto;
import com.gavin.service.MailService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
@RefreshScope
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${mail.from}")
    private String mailFrom;

    private final String mailEncoding = "UTF-8";

    @Retryable(
            value = {MessagingException.class, UnsupportedEncodingException.class, MailException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, maxDelay = 500))
    @Override
    public void sendUserCreatedNotificationMail(UserCreatedMailDto _mailDto) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(mailFrom, "Spring Cloud Demo");
        helper.setTo(_mailDto.getEmail());
        helper.setSubject("您的帐号创建成功");

        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("user_created_notification.ftl");

        Map<String, UserCreatedMailDto> map = new HashMap<>();
        map.put("param", _mailDto);
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        helper.setText(text, true);

        mailSender.send(mimeMessage);
    }

}
