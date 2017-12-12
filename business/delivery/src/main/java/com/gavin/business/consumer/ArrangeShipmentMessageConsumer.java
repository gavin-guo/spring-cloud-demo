package com.gavin.business.consumer;

import com.gavin.business.service.DeliveryService;
import com.gavin.common.consumer.MessageConsumer;
import com.gavin.common.messaging.ArrangeShipmentProcessor;
import com.gavin.common.payload.ArrangeShipmentPayload;
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
public class ArrangeShipmentMessageConsumer implements MessageConsumer<ArrangeShipmentPayload> {

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private DeliveryService deliveryService;

    @StreamListener(ArrangeShipmentProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload ArrangeShipmentPayload _payload) {
        log.info("received arrange_shipment message. {}", JsonUtils.toJson(_payload));

        CompletableFuture
                .runAsync(() -> deliveryService.createDelivery(
                        _payload.getOrderId(),
                        _payload.getConsignee(),
                        _payload.getAddress(),
                        _payload.getPhoneNumber()), executor)
                .thenRunAsync(() -> log.info("arrange shipment for order({}) successfully.", _payload.getOrderId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.error("arrange shipment for order({}) failed.", _payload.getOrderId());
                    return null;
                });
    }

}
