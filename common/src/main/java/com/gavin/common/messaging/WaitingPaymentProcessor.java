package com.gavin.common.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 要求支付的消息通道。
 * order -> payment
 */
public interface WaitingPaymentProcessor {

    String INPUT = "waiting_payment_input";

    String OUTPUT = "waiting_payment_output";

    @Output(WaitingPaymentProcessor.OUTPUT)
    MessageChannel producer();

    @Input(WaitingPaymentProcessor.INPUT)
    SubscribableChannel consumer();

}
