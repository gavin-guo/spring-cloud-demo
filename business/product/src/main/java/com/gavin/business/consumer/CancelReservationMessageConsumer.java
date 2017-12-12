package com.gavin.business.consumer;

import com.gavin.business.service.ProductService;
import com.gavin.common.consumer.MessageConsumer;
import com.gavin.common.messaging.CancelReservationProcessor;
import com.gavin.common.payload.CancelReservationPayload;
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
public class CancelReservationMessageConsumer implements MessageConsumer<CancelReservationPayload> {

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private ProductService productService;

    @StreamListener(CancelReservationProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload CancelReservationPayload _payload) {
        log.info("received cancel_reservation message. {}", JsonUtils.toJson(_payload));

        CompletableFuture
                .runAsync(() -> productService.cancelReservation(_payload.getOrderId()), executor)
                .thenRunAsync(() -> log.info("cancel reservation for order({}) successfully.", _payload.getOrderId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.info("cancel reservation for order({}) failed.", _payload.getOrderId());

                    return null;
                });
    }

}
