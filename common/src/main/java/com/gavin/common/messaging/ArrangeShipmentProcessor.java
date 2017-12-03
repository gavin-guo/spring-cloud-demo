package com.gavin.common.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 订单支付成功后指示发货的消息通道。
 * order -> delivery
 */
public interface ArrangeShipmentProcessor {

    String INPUT = "arrange_shipment_input";

    String OUTPUT = "arrange_shipment_output";

    @Output(ArrangeShipmentProcessor.OUTPUT)
    MessageChannel output();

    @Input(ArrangeShipmentProcessor.INPUT)
    SubscribableChannel input();

}
