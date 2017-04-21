package com.gavin.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 通知支付已成功的消息通道。
 * payment-service -> order-service
 */
public interface PaymentSucceededProcessor {

    String INPUT = "payment_succeeded_input";

    String OUTPUT = "payment_succeeded_output";

    @Output(PaymentSucceededProcessor.OUTPUT)
    MessageChannel output();

    @Input(PaymentSucceededProcessor.INPUT)
    SubscribableChannel input();

}
