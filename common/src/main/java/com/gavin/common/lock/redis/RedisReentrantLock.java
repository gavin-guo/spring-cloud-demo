package com.gavin.common.lock.redis;

import com.gavin.common.lock.DistributedReentrantLock;
import com.google.common.collect.Maps;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisReentrantLock implements DistributedReentrantLock {

    private final ConcurrentMap<Thread, LockData> threadData = Maps.newConcurrentMap();

    private RedisLockInternals internals;

    private String lockKey;

    public RedisReentrantLock(JedisPool jedisPool, String lockKey) {
        this.lockKey = lockKey;
        this.internals = new RedisLockInternals(jedisPool);
    }

    @Override
    public boolean tryLock(long _maxWaitMillis) throws InterruptedException {
        Thread currentThread = Thread.currentThread();

        LockData lockData = threadData.get(currentThread);
        if (lockData != null) {
            lockData.lockCount.incrementAndGet();
            return true;
        }

        String lockValue = internals.trySettingRedisKey(lockKey, _maxWaitMillis);
        // 锁定成功。
        if (lockValue != null) {
            LockData newLockData = new LockData(currentThread, lockValue);
            threadData.put(currentThread, newLockData);
            return true;
        }

        return false;
    }

    @Override
    public void unlock() {
        Thread currentThread = Thread.currentThread();

        LockData lockData = threadData.get(currentThread);
        if (lockData == null) {
            throw new IllegalMonitorStateException(String.format("not owning the lock %s.", lockKey));
        }

        int newLockCount = lockData.lockCount.decrementAndGet();
        if (newLockCount > 0) {
            return;
        }
        if (newLockCount < 0) {
            throw new IllegalMonitorStateException(String.format("lock count has been negative for lock %s.", lockKey));
        }

        try {
            internals.unsetRedisKey(lockKey, lockData.lockValue);
        } finally {
            threadData.remove(currentThread);
        }

    }

    private static class LockData {

        final Thread owningThread;

        final String lockValue;

        final AtomicInteger lockCount = new AtomicInteger(1);

        private LockData(Thread owningThread, String lockValue) {
            this.owningThread = owningThread;
            this.lockValue = lockValue;
        }
    }

}
