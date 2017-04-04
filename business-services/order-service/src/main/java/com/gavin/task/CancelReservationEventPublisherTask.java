package com.gavin.task;

import com.gavin.base.EventPublisherTask;
import com.gavin.constants.DistributedLockKeyConstants;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.CancelReservationEvent;
import com.gavin.lock.DistributedLockCallback;
import com.gavin.lock.DistributedLockTemplate;
import com.gavin.model.PageArgument;
import com.gavin.service.EventService;
import com.gavin.service.OrderService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 向product-service发送取消保留商品的事件消息。
 */
@Component
@Slf4j
public class CancelReservationEventPublisherTask implements EventPublisherTask<CancelReservationEvent> {

    private final Executor executor;

    private final DistributedLockTemplate<List<CancelReservationEvent>> distributedLockTemplate;

    private final EventService<CancelReservationEvent> eventService;

    private final OrderService orderService;

    @Autowired
    public CancelReservationEventPublisherTask(
            @Qualifier("poolTaskExecutor") Executor executor,
            DistributedLockTemplate<List<CancelReservationEvent>> distributedLockTemplate,
            EventService<CancelReservationEvent> eventService,
            OrderService orderService) {
        this.executor = executor;
        this.distributedLockTemplate = distributedLockTemplate;
        this.eventService = eventService;
        this.orderService = orderService;
    }

    @Override
    public List<CancelReservationEvent> pollSomeEventRecords() {
        // 通过分布式锁避免同一个服务的其它实例同时取到相同值。
        return distributedLockTemplate.execute(
                DistributedLockKeyConstants.CANCEL_PRODUCTS_RESERVATION_EVENT,
                2000,
                new DistributedLockCallback<List<CancelReservationEvent>>() {
                    @Override
                    public List<CancelReservationEvent> onLockingSucceeded() throws InterruptedException {
                        PageArgument pageArgument = new PageArgument(0, 20);
                        return eventService.fetchEventsByStatus(
                                MessageableEventStatusEnums.NEW, pageArgument);
                    }

                    @Override
                    public List<CancelReservationEvent> onLockingFailed() throws InterruptedException {
                        return new ArrayList<>();
                    }
                });
    }

    @Scheduled(fixedDelay = 60000)
    @Override
    public void publishEvents() {
        log.info("发布取消保留商品事件的定时任务开始执行。");
        Stopwatch timer = Stopwatch.createStarted();

        // 从数据库中取得指定数量的记录。
        List<CancelReservationEvent> cancelReservationEvents = pollSomeEventRecords();

        if (cancelReservationEvents.isEmpty()) {
            timer.stop();
            log.info("没有需要处理的记录，此次任务执行结束。");
            return;
        }

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        cancelReservationEvents.forEach(
                event -> {
                    CompletableFuture future =
                            CompletableFuture
                                    .supplyAsync(() -> orderService.publishCancelReservationEvent(event), executor)
                                    .thenAcceptAsync(result ->
                                            log.debug("取消保留商品事件（订单ID：{}）发布成功。", event.getOrderId()
                                            ), executor)
                                    .exceptionally(e -> {
                                        log.debug("取消保留商品事件（订单ID：{}）发布失败。", event.getOrderId()
                                        );
                                        log.error(e.getMessage(), e);
                                        return null;
                                    });

                    futures.add(future);
                }
        );

        // 等待异步子任务全部执行完毕。
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();

        timer.stop();
        log.info("本次任务执行结束，共耗费{}毫秒。", timer.elapsed(TimeUnit.MILLISECONDS));
    }

}
