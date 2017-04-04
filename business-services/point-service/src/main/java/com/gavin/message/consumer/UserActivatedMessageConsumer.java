package com.gavin.message.consumer;

import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.UserActivatedEvent;
import com.gavin.messaging.UserActivatedProcessor;
import com.gavin.model.dto.point.ProducePointsDto;
import com.gavin.payload.UserActivatedPayload;
import com.gavin.service.EventService;
import com.gavin.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EnableBinding(UserActivatedProcessor.class)
@Component
@Slf4j
public class UserActivatedMessageConsumer {

    private final Executor executor;

    private final EventService<UserActivatedEvent> eventService;

    private final PointService pointService;

    @Autowired
    public UserActivatedMessageConsumer(
            @Qualifier("poolTaskExecutor") Executor executor,
            EventService<UserActivatedEvent> eventService,
            PointService pointService) {
        this.executor = executor;
        this.eventService = eventService;
        this.pointService = pointService;
    }

    @StreamListener(UserActivatedProcessor.INPUT)
    @Transactional
    public void receiveMessage(@Payload UserActivatedPayload _payload) {
        log.info("接收到用户{}已激活的消息。", _payload.getUserId());

        UserActivatedEvent event = new UserActivatedEvent();
        event.setOriginId(_payload.getEventId());
        event.setUserId(_payload.getUserId());

        eventService.saveEvent(event, MessageableEventStatusEnums.RECEIVED);

        CompletableFuture
                .runAsync(() -> addRewardPoints(event), executor)
                .thenRunAsync(() -> log.debug("用户{}的开户奖励积分已发放成功。", event.getUserId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.debug("用户{}的开户奖励积分发放失败。", event.getUserId());
                    return null;
                });
    }

    @Transactional
    private void addRewardPoints(UserActivatedEvent _event) {
        BigDecimal rewardAmount = new BigDecimal("200");
        ProducePointsDto producePointsDto = new ProducePointsDto();
        producePointsDto.setUserId(_event.getUserId());
        producePointsDto.setAmount(rewardAmount);
        pointService.addPoints(producePointsDto);

        eventService.updateEventStatusByOriginId(
                _event.getOriginId(),
                MessageableEventStatusEnums.PROCESSED);
    }

}
