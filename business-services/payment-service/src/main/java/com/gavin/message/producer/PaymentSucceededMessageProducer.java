package com.gavin.message.producer;

import com.gavin.base.MessageProducer;
import com.gavin.event.PaymentSucceededEvent;
import com.gavin.messaging.PaymentSucceededProcessor;
import com.gavin.payload.PaymentSucceededPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding(PaymentSucceededProcessor.class)
@Component
public class PaymentSucceededMessageProducer implements MessageProducer<PaymentSucceededEvent> {

    private final PaymentSucceededProcessor source;

    @Autowired
    public PaymentSucceededMessageProducer(
            PaymentSucceededProcessor source) {
        this.source = source;
    }

    @Override
    public boolean sendMessage(PaymentSucceededEvent _event) {
        PaymentSucceededPayload payload = new PaymentSucceededPayload();
        payload.setEventId(_event.getOriginId());
        payload.setOrderId(_event.getOrderId());

        Message<PaymentSucceededPayload> message = MessageBuilder.withPayload(payload).build();
        return source.output().send(message);
    }

}
