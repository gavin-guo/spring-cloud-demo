package com.gavin.common.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 取消保留商品的消息通道。
 * order -> product
 */
public interface CancelReservationProcessor {

    String INPUT = "cancel_reservation_input";

    String OUTPUT = "cancel_reservation_output";

    @Output(CancelReservationProcessor.OUTPUT)
    MessageChannel output();

    @Input(CancelReservationProcessor.INPUT)
    SubscribableChannel input();

}
