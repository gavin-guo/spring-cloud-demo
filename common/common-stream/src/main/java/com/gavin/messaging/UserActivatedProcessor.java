package com.gavin.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 通知用户已激活的消息通道。
 * user-service -> point-service
 */
public interface UserActivatedProcessor {

    String INPUT = "user_activated_input";

    String OUTPUT = "user_activated_output";

    @Output(UserActivatedProcessor.OUTPUT)
    MessageChannel output();

    @Input(UserActivatedProcessor.INPUT)
    SubscribableChannel input();

}
