package com.gavin.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 要求支付的消息通道。
 * order -> payment
 */
public interface WaitingForPaymentProcessor {

    String INPUT = "waiting_for_payment_input";

    String OUTPUT = "waiting_for_payment_output";

    @Output(WaitingForPaymentProcessor.OUTPUT)
    MessageChannel output();

    @Input(WaitingForPaymentProcessor.INPUT)
    SubscribableChannel input();

}
