package com.gavin.common.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * 通知用户已激活的消息通道。
 * user -> point
 */
public interface RewardPointsProcessor {

    String INPUT = "reward_points_input";

    String OUTPUT = "reward_points_output";

    @Output(RewardPointsProcessor.OUTPUT)
    MessageChannel producer();

    @Input(RewardPointsProcessor.INPUT)
    SubscribableChannel consumer();

}
