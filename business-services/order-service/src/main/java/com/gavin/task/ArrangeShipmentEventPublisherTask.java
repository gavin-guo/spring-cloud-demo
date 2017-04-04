package com.gavin.task;

import com.gavin.base.EventPublisherTask;
import com.gavin.constants.DistributedLockKeyConstants;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.ArrangeShipmentEvent;
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
 * 向delivery-service发送安排发货的事件消息。
 */
@Component
@Slf4j
public class ArrangeShipmentEventPublisherTask implements EventPublisherTask<ArrangeShipmentEvent> {

    private final Executor executor;

    private final DistributedLockTemplate<List<ArrangeShipmentEvent>> distributedLockTemplate;

    private final EventService<ArrangeShipmentEvent> eventService;

    private final OrderService orderService;

    @Autowired
    public ArrangeShipmentEventPublisherTask(
            @Qualifier("poolTaskExecutor") Executor executor,
            DistributedLockTemplate<List<ArrangeShipmentEvent>> distributedLockTemplate,
            EventService<ArrangeShipmentEvent> eventService,
            OrderService orderService) {
        this.executor = executor;
        this.distributedLockTemplate = distributedLockTemplate;
        this.eventService = eventService;
        this.orderService = orderService;
    }

    @Override
    public List<ArrangeShipmentEvent> pollSomeEventRecords() {
        // 通过分布式锁避免同一个服务的其它实例同时取到相同值。
        return distributedLockTemplate.execute(
                DistributedLockKeyConstants.ARRANGE_SHIPMENT_EVENT,
                2000,
                new DistributedLockCallback<List<ArrangeShipmentEvent>>() {
                    @Override
                    public List<ArrangeShipmentEvent> onLockingSucceeded() throws InterruptedException {
                        PageArgument pageArgument = new PageArgument(0, 20);
                        return eventService.fetchEventsByStatus(
                                MessageableEventStatusEnums.NEW, pageArgument);
                    }

                    @Override
                    public List<ArrangeShipmentEvent> onLockingFailed() throws InterruptedException {
                        return new ArrayList<>();
                    }
                });
    }

    @Scheduled(fixedDelay = 60000)
    @Override
    public void publishEvents() {
        log.info("发布安排发货事件的定时任务开始执行。");
        Stopwatch timer = Stopwatch.createStarted();

        // 从数据库中取得指定数量的记录。
        List<ArrangeShipmentEvent> arrangeShipmentEvents = pollSomeEventRecords();

        if (arrangeShipmentEvents.isEmpty()) {
            timer.stop();
            log.info("没有需要处理的记录，此次任务执行结束。");
            return;
        }

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        arrangeShipmentEvents.forEach(
                event -> {
                    CompletableFuture future =
                            CompletableFuture
                                    .supplyAsync(() -> orderService.publishArrangeShipmentEvent(event), executor)
                                    .thenAcceptAsync(result ->
                                            log.debug("安排发货事件（订单ID：{}）发布成功。", event.getOrderId()
                                            ), executor)
                                    .exceptionally(e -> {
                                        log.debug("安排发货事件（订单ID：{}）发布失败。", event.getOrderId()
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
