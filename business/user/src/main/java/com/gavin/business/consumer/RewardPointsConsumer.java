package com.gavin.business.consumer;

import com.gavin.business.service.PointService;
import com.gavin.common.consumer.MessageConsumer;
import com.gavin.common.dto.user.RewardPointsDto;
import com.gavin.common.messaging.RewardPointsProcessor;
import com.gavin.common.payload.RewardPointsPayload;
import com.gavin.common.util.JsonUtils;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RewardPointsConsumer implements MessageConsumer<RewardPointsPayload> {

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private PointService pointService;

    @StreamListener(RewardPointsProcessor.INPUT)
    @Transactional
    public void consumeMessage(@Payload RewardPointsPayload _payload) {
        log.info("received reward_points message. {}", JsonUtils.toJson(_payload));

        RewardPointsDto pointsDto = new RewardPointsDto();
        pointsDto.setUserId(_payload.getUserId());
        pointsDto.setAmount(_payload.getAmount());
        pointsDto.setReason(_payload.getReason());

        log.info("start processing reward_points message.");
        Stopwatch timer = Stopwatch.createStarted();

        CompletableFuture future =
                CompletableFuture
                        .runAsync(() -> pointService.addPoints(pointsDto), executor)
                        .thenRunAsync(() ->
                                        log.info("reward user({}) {} points successfully.", pointsDto.getUserId(), pointsDto.getAmount())
                                , executor)
                        .exceptionally(e -> {
                            log.error(e.getMessage(), e);
                            log.info("reward user({}) {} points failed.", pointsDto.getUserId(), pointsDto.getAmount());
                            return null;
                        });

        // 等待异步子任务全部执行完毕。
        CompletableFuture.allOf(future).join();
        timer.stop();

        log.info("finished processing reward_points message, elapsed {} milliseconds. ", timer.elapsed(TimeUnit.MILLISECONDS));
    }

}
