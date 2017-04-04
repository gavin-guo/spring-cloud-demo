package com.gavin.message.producer;

import com.gavin.base.MessageProducer;
import com.gavin.event.ArrangeShipmentEvent;
import com.gavin.messaging.ArrangeShipmentProcessor;
import com.gavin.payload.ArrangeShipmentPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding(ArrangeShipmentProcessor.class)
@Component
public class ArrangeShipmentMessageProducer implements MessageProducer<ArrangeShipmentEvent> {

    private final ArrangeShipmentProcessor source;

    @Autowired
    public ArrangeShipmentMessageProducer(ArrangeShipmentProcessor source) {
        this.source = source;
    }

    @Override
    public boolean sendMessage(ArrangeShipmentEvent _event) {
        ArrangeShipmentPayload payload = new ArrangeShipmentPayload();
        payload.setEventId(_event.getOriginId());
        payload.setOrderId(_event.getOrderId());
        payload.setConsignee(_event.getConsignee());
        payload.setAddress(_event.getAddress());
        payload.setPhoneNumber(_event.getPhoneNumber());

        Message<ArrangeShipmentPayload> message = MessageBuilder.withPayload(payload).build();
        return source.output().send(message);
    }

}
