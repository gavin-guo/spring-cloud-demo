package com.gavin.message.producer;

import com.gavin.base.MessageProducer;
import com.gavin.event.WaitingForPaymentEvent;
import com.gavin.messaging.WaitingForPaymentProcessor;
import com.gavin.payload.WaitingForPaymentPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding(WaitingForPaymentProcessor.class)
@Component
public class WaitingForPaymentMessageProducer implements MessageProducer<WaitingForPaymentEvent> {

    private final WaitingForPaymentProcessor source;

    @Autowired
    public WaitingForPaymentMessageProducer(WaitingForPaymentProcessor source) {
        this.source = source;
    }

    @Override
    public boolean sendMessage(WaitingForPaymentEvent _event) {
        WaitingForPaymentPayload payload = new WaitingForPaymentPayload();
        payload.setEventId(_event.getOriginId());
        payload.setUserId(_event.getUserId());
        payload.setOrderId(_event.getOrderId());
        payload.setAmount(_event.getAmount());

        Message<WaitingForPaymentPayload> message = MessageBuilder.withPayload(payload).build();
        return source.output().send(message);
    }

}
