package com.gavin.consumer;

import com.gavin.base.MessageConsumer;
import com.gavin.messaging.PaymentSucceededProcessor;
import com.gavin.payload.PaymentSucceededPayload;
import com.gavin.service.OrderService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class PaymentSucceededMessageConsumer implements MessageConsumer<PaymentSucceededPayload> {

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OrderService orderService;

    @StreamListener(PaymentSucceededProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload PaymentSucceededPayload _payload) {
        log.info("received payment_succeeded message. {}", new Gson().toJson(_payload));

        CompletableFuture
                .runAsync(() ->
                        orderService.succeedInPayment(_payload.getOrderId()
                        ), executor)
                .thenRunAsync(() -> {
                    log.info("update status of order({}) to paid successfully.", _payload.getOrderId());
                }, executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.warn("update status of order({}) to paid failed.", _payload.getOrderId());
                    return null;
                });
    }

}
