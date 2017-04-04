package com.gavin.message.producer;

import com.gavin.base.MessageProducer;
import com.gavin.event.CancelReservationEvent;
import com.gavin.messaging.CancelReservationProcessor;
import com.gavin.payload.CancelReservationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding(CancelReservationProcessor.class)
@Component
public class CancelReservationMessageProducer implements MessageProducer<CancelReservationEvent> {

    private final CancelReservationProcessor source;

    @Autowired
    public CancelReservationMessageProducer(CancelReservationProcessor source) {
        this.source = source;
    }

    @Override
    public boolean sendMessage(CancelReservationEvent _event) {
        CancelReservationPayload payload = new CancelReservationPayload();
        payload.setEventId(_event.getOriginId());
        payload.setOrderId(_event.getOrderId());

        Message<CancelReservationPayload> message = MessageBuilder.withPayload(payload).build();
        return source.output().send(message);
    }

}
