package com.gavin.task;

import com.gavin.base.EventPublisherTask;
import com.gavin.constants.DistributedLockKeyConstants;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.WaitingForPaymentEvent;
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
 * 向payment-service发送生成支付请求的事件消息。
 */
@Component
@Slf4j
public class WaitingForPaymentEventPublisherTask implements EventPublisherTask<WaitingForPaymentEvent> {

    private final Executor executor;

    private final DistributedLockTemplate<List<WaitingForPaymentEvent>> distributedLockTemplate;

    private final EventService<WaitingForPaymentEvent> eventService;

    private final OrderService orderService;

    @Autowired
    public WaitingForPaymentEventPublisherTask(
            @Qualifier("poolTaskExecutor") Executor executor,
            DistributedLockTemplate<List<WaitingForPaymentEvent>> distributedLockTemplate,
            EventService<WaitingForPaymentEvent> eventService,
            OrderService orderService) {
        this.executor = executor;
        this.distributedLockTemplate = distributedLockTemplate;
        this.eventService = eventService;
        this.orderService = orderService;
    }

    @Override
    public List<WaitingForPaymentEvent> pollSomeEventRecords() {
        // 通过分布式锁避免同一个服务的其它实例同时取到相同值。
        return distributedLockTemplate.execute(
                DistributedLockKeyConstants.WAITING_FOR_PAYMENT_EVENT,
                2000,
                new DistributedLockCallback<List<WaitingForPaymentEvent>>() {
                    @Override
                    public List<WaitingForPaymentEvent> onLockingSucceeded() throws InterruptedException {
                        PageArgument pageArgument = new PageArgument(0, 20);
                        return eventService.fetchEventsByStatus(
                                MessageableEventStatusEnums.NEW, pageArgument);
                    }

                    @Override
                    public List<WaitingForPaymentEvent> onLockingFailed() throws InterruptedException {
                        return new ArrayList<>();
                    }
                });
    }

    @Scheduled(fixedDelay = 60000)
    @Override
    public void publishEvents() {
        log.info("发布生成支付请求事件的定时任务开始执行。");
        Stopwatch timer = Stopwatch.createStarted();

        // 从数据库中取得指定数量的记录。
        List<WaitingForPaymentEvent> waitingForPaymentEvents = pollSomeEventRecords();

        if (waitingForPaymentEvents.isEmpty()) {
            timer.stop();
            log.info("没有需要处理的记录，此次任务执行结束。");
            return;
        }

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        waitingForPaymentEvents.forEach(
                event -> {
                    CompletableFuture future =
                            CompletableFuture
                                    .supplyAsync(() -> orderService.publishWaitingForPaymentEvent(event), executor)
                                    .thenAcceptAsync(result ->
                                            log.debug("生成支付请求事件（订单ID：{}）发布成功。", event.getOrderId()
                                            ), executor)
                                    .exceptionally(e -> {
                                        log.debug("生成支付请求事件（订单ID：{}）发布失败。", event.getOrderId()
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
