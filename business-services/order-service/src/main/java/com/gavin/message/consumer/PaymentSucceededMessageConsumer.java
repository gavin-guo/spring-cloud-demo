package com.gavin.message.consumer;

import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.PaymentSucceededEvent;
import com.gavin.messaging.PaymentSucceededProcessor;
import com.gavin.payload.PaymentSucceededPayload;
import com.gavin.service.EventService;
import com.gavin.service.OrderService;
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

@EnableBinding(PaymentSucceededProcessor.class)
@Component
@Slf4j
public class PaymentSucceededMessageConsumer {

    private final Executor executor;

    private final ModelMapper modelMapper;

    private final EventService<PaymentSucceededEvent> eventService;

    private final OrderService orderService;

    @Autowired
    public PaymentSucceededMessageConsumer(
            @Qualifier("poolTaskExecutor") Executor executor,
            ModelMapper modelMapper,
            EventService<PaymentSucceededEvent> eventService,
            OrderService orderService) {
        this.executor = executor;
        this.modelMapper = modelMapper;
        this.eventService = eventService;
        this.orderService = orderService;
    }

    @StreamListener(PaymentSucceededProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload PaymentSucceededPayload _payload) {
        log.info("接收到订单{}支付成功的消息。", _payload.getOrderId());

        PaymentSucceededEvent event = modelMapper.map(_payload, PaymentSucceededEvent.class);
        event.setOriginId(_payload.getEventId());

        eventService.saveEvent(event, MessageableEventStatusEnums.RECEIVED);

        CompletableFuture
                .runAsync(() ->
                        orderService.succeedInPayment(event.getOrderId()
                        ), executor)
                .thenRunAsync(() -> {
                    log.debug("订单{}已支付的状态修改成功。", event.getOrderId());
                    eventService.updateEventStatusByOriginId(
                            event.getOriginId(),
                            MessageableEventStatusEnums.PROCESSED);
                }, executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.debug("订单{}已支付的状态修改失败。", event.getOrderId());
                    eventService.updateEventStatusByOriginId(
                            event.getOriginId(),
                            MessageableEventStatusEnums.FAILED);
                    return null;
                });
    }

}
