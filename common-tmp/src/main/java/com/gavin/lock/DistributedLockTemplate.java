package com.gavin.lock;

public interface DistributedLockTemplate<T> {

    /**
     * @param _lockKey       锁的唯一标识
     * @param _maxWaitMillis 获取排它锁时的最大等待时间（单位：毫秒）
     * @param _callback
     * @return
     */
    T execute(String _lockKey, long _maxWaitMillis, DistributedLockCallback<T> _callback);

}
