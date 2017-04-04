package com.gavin.message.consumer;

import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.ArrangeShipmentEvent;
import com.gavin.messaging.ArrangeShipmentProcessor;
import com.gavin.payload.ArrangeShipmentPayload;
import com.gavin.service.DeliveryService;
import com.gavin.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EnableBinding(ArrangeShipmentProcessor.class)
@Component
@Slf4j
public class ArrangeShipmentMessageConsumer {

    private final ModelMapper modelMapper;

    private final Executor executor;

    private final EventService<ArrangeShipmentEvent> eventService;

    private final DeliveryService deliveryService;

    @Autowired
    public ArrangeShipmentMessageConsumer(
            ModelMapper modelMapper,
            @Qualifier("poolTaskExecutor") Executor executor,
            EventService<ArrangeShipmentEvent> eventService,
            DeliveryService deliveryService) {
        this.modelMapper = modelMapper;
        this.executor = executor;
        this.eventService = eventService;
        this.deliveryService = deliveryService;
    }

    @StreamListener(ArrangeShipmentProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload ArrangeShipmentPayload _payload) {
        log.info("接收到给订单{}安排发货的消息。", _payload.getOrderId());

        ArrangeShipmentEvent event = modelMapper.map(_payload, ArrangeShipmentEvent.class);
        event.setOriginId(_payload.getEventId());

        eventService.saveEvent(event, MessageableEventStatusEnums.RECEIVED);

        CompletableFuture
                .runAsync(() -> startArrangeShipmentEvent(event), executor)
                .thenRunAsync(() -> log.debug("订单{}的物流记录创建成功。", event.getOrderId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.debug("订单{}的物流记录创建失败。", event.getOrderId());
                    return null;
                });
    }

    @Transactional
    private void startArrangeShipmentEvent(ArrangeShipmentEvent _event) {
        deliveryService.createDelivery(
                _event.getOrderId(),
                _event.getConsignee(),
                _event.getAddress(),
                _event.getPhoneNumber());

        eventService.updateEventStatusByOriginId(
                _event.getOriginId(),
                MessageableEventStatusEnums.PROCESSED);
    }

}
