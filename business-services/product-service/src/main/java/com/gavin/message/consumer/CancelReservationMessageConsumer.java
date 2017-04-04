package com.gavin.message.consumer;

import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.CancelReservationEvent;
import com.gavin.messaging.CancelReservationProcessor;
import com.gavin.payload.CancelReservationPayload;
import com.gavin.service.EventService;
import com.gavin.service.ProductService;
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

@EnableBinding(CancelReservationProcessor.class)
@Component
@Slf4j
public class CancelReservationMessageConsumer {

    private final Executor executor;

    private final ModelMapper modelMapper;

    private final EventService<CancelReservationEvent> eventService;

    private final ProductService productService;

    @Autowired
    public CancelReservationMessageConsumer(
            @Qualifier("poolTaskExecutor") Executor executor,
            ModelMapper modelMapper,
            EventService<CancelReservationEvent> eventService,
            ProductService productService) {
        this.executor = executor;
        this.modelMapper = modelMapper;
        this.eventService = eventService;
        this.productService = productService;
    }

    @StreamListener(CancelReservationProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload CancelReservationPayload _payload) {
        log.info("接收到取消订单{}中保留商品的消息。", _payload.getOrderId());

        CancelReservationEvent event = modelMapper.map(_payload, CancelReservationEvent.class);
        event.setOriginId(_payload.getEventId());

        eventService.saveEvent(event, MessageableEventStatusEnums.RECEIVED);

        CompletableFuture
                .runAsync(() -> productService.cancelReservation(_payload.getOrderId()), executor)
                .thenRunAsync(() -> {
                    log.debug("订单{}中保留的商品取消成功。", event.getOrderId());
                    eventService.updateEventStatusByOriginId(event.getOriginId(),
                            MessageableEventStatusEnums.PROCESSED);
                }, executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.debug("订单{}中保留的商品取消失败。", event.getOrderId());
                    eventService.updateEventStatusByOriginId(event.getOriginId(),
                            MessageableEventStatusEnums.FAILED);

                    return null;
                });
    }

}
