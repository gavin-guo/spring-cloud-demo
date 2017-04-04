package com.gavin.message.producer;

import com.gavin.base.MessageProducer;
import com.gavin.event.UserActivatedEvent;
import com.gavin.messaging.UserActivatedProcessor;
import com.gavin.payload.UserActivatedPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding(UserActivatedProcessor.class)
@Component
public class UserActivatedMessageProducer implements MessageProducer<UserActivatedEvent> {

    private final UserActivatedProcessor source;

    @Autowired
    public UserActivatedMessageProducer(
            UserActivatedProcessor source) {
        this.source = source;
    }

    @Override
    public boolean sendMessage(UserActivatedEvent _event) {
        UserActivatedPayload payload = new UserActivatedPayload();
        payload.setEventId(_event.getOriginId());
        payload.setUserId(_event.getUserId());

        Message<UserActivatedPayload> message = MessageBuilder.withPayload(payload).build();
        return source.output().send(message);
    }

}
