package com.gavin.task;

import com.gavin.base.EventPublisherTask;
import com.gavin.constants.DistributedLockKeyConstants;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.PaymentSucceededEvent;
import com.gavin.lock.DistributedLockCallback;
import com.gavin.lock.DistributedLockTemplate;
import com.gavin.model.PageArgument;
import com.gavin.service.EventService;
import com.gavin.service.PaymentService;
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
 * 向order-service发送支付成功的事件消息。
 */
@Component
@Slf4j
public class PaymentSucceededEventPublisherTask implements EventPublisherTask<PaymentSucceededEvent> {

    private final Executor executor;

    private final DistributedLockTemplate<List<PaymentSucceededEvent>> distributedLockTemplate;

    private final EventService<PaymentSucceededEvent> eventService;

    private final PaymentService paymentService;

    @Autowired
    public PaymentSucceededEventPublisherTask(
            @Qualifier("poolTaskExecutor") Executor executor,
            DistributedLockTemplate<List<PaymentSucceededEvent>> distributedLockTemplate,
            EventService<PaymentSucceededEvent> eventService,
            PaymentService paymentService) {
        this.executor = executor;
        this.distributedLockTemplate = distributedLockTemplate;
        this.eventService = eventService;
        this.paymentService = paymentService;
    }

    @Override
    public List<PaymentSucceededEvent> pollSomeEventRecords() {
        // 通过分布式锁避免同一个服务的其它实例同时取到相同值。
        return distributedLockTemplate.execute(
                DistributedLockKeyConstants.PAYMENT_SUCCEEDED_EVENT,
                2000,
                new DistributedLockCallback<List<PaymentSucceededEvent>>() {
                    @Override
                    public List<PaymentSucceededEvent> onLockingSucceeded() throws InterruptedException {
                        PageArgument pageArgument = new PageArgument(0, 10);
                        return eventService.fetchEventsByStatus(
                                MessageableEventStatusEnums.NEW, pageArgument);
                    }

                    @Override
                    public List<PaymentSucceededEvent> onLockingFailed() throws InterruptedException {
                        return new ArrayList<>();
                    }
                });
    }

    @Scheduled(fixedDelay = 60000)
    @Override
    public void publishEvents() {
        log.info("发布支付成功事件的定时任务开始执行。");
        Stopwatch timer = Stopwatch.createStarted();

        // 从数据库中取得指定数量的记录。
        List<PaymentSucceededEvent> paymentSucceededEvents = pollSomeEventRecords();

        if (paymentSucceededEvents.isEmpty()) {
            timer.stop();
            log.info("没有需要处理的记录，此次任务执行结束。");
            return;
        }

        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        paymentSucceededEvents.forEach(
                event -> {
                    CompletableFuture future =
                            CompletableFuture
                                    .supplyAsync(() -> paymentService.publishPaymentSucceededEvent(event), executor)
                                    .thenAcceptAsync(
                                            result -> log.debug("支付成功事件（订单ID：{}）发布成功。", event.getOrderId()
                                            ), executor)
                                    .exceptionally(e -> {
                                        log.debug("支付成功事件（订单ID：{}）发布失败。", event.getOrderId());
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
