package com.gavin.business.consumer;

import com.gavin.business.service.OrderService;
import com.gavin.common.consumer.MessageConsumer;
import com.gavin.common.messaging.PaymentSucceededProcessor;
import com.gavin.common.payload.PaymentSucceededPayload;
import com.gavin.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
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
    private OrderService orderService;

    @StreamListener(PaymentSucceededProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload PaymentSucceededPayload _payload) {
        log.info("received payment_succeeded message. {}", JsonUtils.toJson(_payload));

        CompletableFuture
                .runAsync(() ->
                        orderService.succeedInPayment(_payload.getOrderId()
                        ), executor)
                .thenRunAsync(() -> log.info("update the status of order({}) to 'PAID' successfully.", _payload.getOrderId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.warn("update the status of order({}) to 'PAID' failed.", _payload.getOrderId());
                    return null;
                });
    }

}
