package com.gavin.business.consumer;

import com.gavin.business.service.PointService;
import com.gavin.common.consumer.MessageConsumer;
import com.gavin.common.dto.user.ProducePointsDto;
import com.gavin.common.messaging.UserActivatedProcessor;
import com.gavin.common.payload.UserActivatedPayload;
import com.gavin.common.util.JsonUtils;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class UserActivatedMessageConsumer implements MessageConsumer<UserActivatedPayload> {

    @Value("${points.user-activated-rewards}")
    private Integer rewards;

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private PointService pointService;

    @StreamListener(UserActivatedProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload UserActivatedPayload _payload) {
        log.info("received user_activated message. {}", JsonUtils.toJson(_payload));

        ProducePointsDto pointsDto = new ProducePointsDto();
        pointsDto.setUserId(_payload.getUserId());
        pointsDto.setAmount(new BigDecimal(rewards));

        log.info("start processing user_activated message.");
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

        log.info("finished processing user_activated message, elapsed {} milliseconds. ", timer.elapsed(TimeUnit.MILLISECONDS));
    }

}
