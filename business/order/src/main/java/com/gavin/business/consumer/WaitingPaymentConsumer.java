package com.gavin.business.consumer;

import com.gavin.business.service.PaymentService;
import com.gavin.common.consumer.MessageConsumer;
import com.gavin.common.messaging.WaitingPaymentProcessor;
import com.gavin.common.payload.WaitingPaymentPayload;
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
public class WaitingPaymentConsumer implements MessageConsumer<WaitingPaymentPayload> {

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private PaymentService paymentService;

    @StreamListener(WaitingPaymentProcessor.INPUT)
    @Transactional
    public void consumeMessage(@Payload WaitingPaymentPayload _payload) {
        log.info("received waiting_for_payment message. {}", JsonUtils.toJson(_payload));

        CompletableFuture
                .supplyAsync(() -> paymentService.createPayment(
                        _payload.getUserId(),
                        _payload.getOrderId(),
                        _payload.getAmount()
                ), executor)
                .thenAcceptAsync(paymentId ->
                                log.info("create payment for order({}) successfully. payment_id={}", _payload.getOrderId(), paymentId)
                        , executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.error("create payment for order({}) failed.", _payload.getOrderId());

                    return null;
                });
    }

}
