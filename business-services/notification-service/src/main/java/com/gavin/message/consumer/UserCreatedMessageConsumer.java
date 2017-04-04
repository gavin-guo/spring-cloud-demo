package com.gavin.message.consumer;

import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.UserCreatedEvent;
import com.gavin.exception.EmailSendingException;
import com.gavin.messaging.UserCreatedProcessor;
import com.gavin.payload.UserCreatedPayload;
import com.gavin.service.EventService;
import com.gavin.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EnableBinding(UserCreatedProcessor.class)
@Component
@Slf4j
public class UserCreatedMessageConsumer {

    private final Executor executor;

    private final ModelMapper modelMapper;

    private final MailService mailService;

    private final EventService<UserCreatedEvent> eventService;

    @Autowired
    public UserCreatedMessageConsumer(
            @Qualifier("poolTaskExecutor") Executor executor,
            ModelMapper modelMapper,
            MailService mailService,
            EventService<UserCreatedEvent> eventService) {
        this.executor = executor;
        this.modelMapper = modelMapper;
        this.mailService = mailService;
        this.eventService = eventService;
    }

    @StreamListener(UserCreatedProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload UserCreatedPayload _payload) {
        log.info("接收到用户{}已创建的消息。", _payload.getUserId());

        UserCreatedEvent event = modelMapper.map(_payload, UserCreatedEvent.class);
        event.setOriginId(_payload.getEventId());

        eventService.saveEvent(event, MessageableEventStatusEnums.RECEIVED);

        CompletableFuture
                .runAsync(() -> sendUserCreatedMail(event), executor)
                .thenRunAsync(() -> log.debug("用户{}已创建的通知邮件发送成功。", event.getUserId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.debug("用户{}已创建的通知邮件发送失败。", event.getUserId());
                    return null;
                });
    }

    @Transactional
    private void sendUserCreatedMail(UserCreatedEvent _event) {
        try {
            mailService.sendUserCreatedNotificationMail(_event);

            eventService.updateEventStatusByOriginId(
                    _event.getOriginId(),
                    MessageableEventStatusEnums.PROCESSED);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new EmailSendingException();
        }
    }

}
