package com.gavin.task;

import com.gavin.constants.DistributedLockKeyConstants;
import com.gavin.lock.DistributedLockCallback;
import com.gavin.lock.DistributedLockTemplate;
import com.gavin.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 清除过期积分。
 */
@Component
@Slf4j
public class ExpiredPointsCleanerTask {

    private final DistributedLockTemplate<Boolean> distributedLockTemplate;

    private final PointService pointService;

    @Autowired
    public ExpiredPointsCleanerTask(
            DistributedLockTemplate<Boolean> distributedLockTemplate,
            PointService pointService) {
        this.distributedLockTemplate = distributedLockTemplate;
        this.pointService = pointService;
    }

    @Scheduled(cron = "0 0 23 * * ?")
    public void cleanExpiredPoints() {
        distributedLockTemplate.execute(
                DistributedLockKeyConstants.CLEAN_EXPIRED_POINTS_EVENT,
                2000,
                new DistributedLockCallback<Boolean>() {
                    @Override
                    public Boolean onLockingSucceeded() throws InterruptedException {
                        pointService.cleanExpiredPoints();
                        return true;
                    }

                    @Override
                    public Boolean onLockingFailed() throws InterruptedException {
                        return false;
                    }
                });
    }

}
