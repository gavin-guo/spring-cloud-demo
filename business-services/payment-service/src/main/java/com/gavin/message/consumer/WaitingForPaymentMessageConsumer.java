package com.gavin.message.consumer;

import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.WaitingForPaymentEvent;
import com.gavin.messaging.WaitingForPaymentProcessor;
import com.gavin.payload.WaitingForPaymentPayload;
import com.gavin.service.EventService;
import com.gavin.service.PaymentService;
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

@EnableBinding(WaitingForPaymentProcessor.class)
@Component
@Slf4j
public class WaitingForPaymentMessageConsumer {

    private final Executor executor;

    private final ModelMapper modelMapper;

    private final EventService<WaitingForPaymentEvent> eventService;

    private final PaymentService paymentService;

    @Autowired
    public WaitingForPaymentMessageConsumer(
            @Qualifier("poolTaskExecutor") Executor executor,
            ModelMapper modelMapper,
            EventService<WaitingForPaymentEvent> eventService,
            PaymentService paymentService) {
        this.executor = executor;
        this.modelMapper = modelMapper;
        this.eventService = eventService;
        this.paymentService = paymentService;
    }

    @StreamListener(WaitingForPaymentProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload WaitingForPaymentPayload _payload) {
        log.info("接收到请求支付订单{}的消息。", _payload.getOrderId());

        WaitingForPaymentEvent event = modelMapper.map(_payload, WaitingForPaymentEvent.class);
        event.setOriginId(_payload.getEventId());

        eventService.saveEvent(event, MessageableEventStatusEnums.RECEIVED);

        CompletableFuture
                .runAsync(() -> paymentService.createPayment(
                        event.getUserId(),
                        event.getOrderId(),
                        event.getAmount()
                ), executor)
                .thenRunAsync(() -> {
                    log.debug("生成订单{}的支付记录成功。", event.getOrderId());
                    eventService.updateEventStatusByOriginId(event.getOriginId(),
                            MessageableEventStatusEnums.PROCESSED);
                }, executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.debug("生成订单{}的支付记录失败。", event.getOrderId());
                    eventService.updateEventStatusByOriginId(event.getOriginId(),
                            MessageableEventStatusEnums.FAILED);

                    return null;
                });
    }

}
