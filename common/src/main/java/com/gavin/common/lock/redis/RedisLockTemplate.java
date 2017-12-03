package com.gavin.common.lock.redis;

import com.gavin.common.lock.DistributedLockCallback;
import com.gavin.common.lock.DistributedLockTemplate;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

@Slf4j
public class RedisLockTemplate<T> implements DistributedLockTemplate<T> {

    private JedisPool jedisPool;

    public RedisLockTemplate(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public T execute(String _lockKey, long _maxWaitMillis, DistributedLockCallback<T> _callback) {
        RedisReentrantLock lock = null;
        boolean gotLock = false;

        try {
            lock = new RedisReentrantLock(jedisPool, _lockKey);

            if (lock.tryLock(_maxWaitMillis)) {
                gotLock = true;
                return _callback.onLockingSucceeded();
            } else {
                return _callback.onLockingFailed();
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (gotLock) {
                lock.unlock();
            }
        }
        return null;
    }

}
