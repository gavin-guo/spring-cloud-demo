package com.gavin.consumer;

import com.gavin.base.MessageConsumer;
import com.gavin.exception.EmailSendException;
import com.gavin.messaging.UserCreatedProcessor;
import com.gavin.model.UserCreatedMailDto;
import com.gavin.payload.UserCreatedPayload;
import com.gavin.service.MailService;
import com.google.gson.Gson;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Component
@Slf4j
public class UserCreatedMessageConsumer implements MessageConsumer<UserCreatedPayload> {

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MailService mailService;

    @StreamListener(UserCreatedProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload UserCreatedPayload _payload) {
        log.info("received user_created message ({}).", new Gson().toJson(_payload));

        UserCreatedMailDto mailDto = modelMapper.map(_payload, UserCreatedMailDto.class);

        CompletableFuture
                .runAsync(() -> {
                    try {
                        mailService.sendUserCreatedNotificationMail(mailDto);
                    } catch (MessagingException | TemplateException | IOException e) {
                        throw new EmailSendException(e);
                    }
                }, executor)
                .thenRunAsync(() -> log.info("send user_created notification email successfully. user_id = {}", mailDto.getUserId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.warn("send user_created notification email failed. user_id={}", mailDto.getUserId());
                    return null;
                });
    }

}
