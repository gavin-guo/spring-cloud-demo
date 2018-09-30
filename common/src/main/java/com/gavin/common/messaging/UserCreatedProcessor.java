package com.gavin.common.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 通知用户创建成功的消息通道。
 * user -> notification
 */
public interface UserCreatedProcessor {

    String INPUT = "user_created_input";

    String OUTPUT = "user_created_output";

    @Output(UserCreatedProcessor.OUTPUT)
    MessageChannel producer();

    @Input(UserCreatedProcessor.INPUT)
    SubscribableChannel consumer();

}
