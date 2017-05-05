package com.gavin.lock.redis;

import org.apache.commons.lang.RandomStringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class RedisLockInternals {

    private JedisPool jedisPool;

    /**
     * 重试间隔时间（单位：毫秒）
     */
    private final int retryIntervalMillis = 300;

    /**
     * 自动失效时间（单位：毫秒）
     */
    private final long expireMillis = 60000;

    public RedisLockInternals(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    String trySettingRedisKey(String _lockKey, long _maxWaitMillis) {
        final long startMillis = System.currentTimeMillis();

        String lockValue;
        while (true) {
            lockValue = setRedisKey(_lockKey);
            // 成功获取到锁。
            if (lockValue != null) {
                break;
            }
            // 超过最大等待时间也未获取到锁。
            if (System.currentTimeMillis() - startMillis - retryIntervalMillis > _maxWaitMillis) {
                break;
            }

            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(retryIntervalMillis));
        }

        return lockValue;
    }

    private String setRedisKey(String _lockKey) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = _lockKey + "-" + RandomStringUtils.randomAlphanumeric(10);

            if (jedis.setnx(_lockKey, value).intValue() == 1) {
                jedis.pexpire(_lockKey, expireMillis);
                return value;
            } else {
                return null;
            }
        }

    }

    void unsetRedisKey(String _lockKey, String _value) {
        try (Jedis jedis = jedisPool.getResource()) {
            if (_value.equals(jedis.get(_lockKey))) {
                jedis.del(_lockKey);
            }
        }
    }

}
