package com.gavin.lock;

public interface DistributedReentrantLock {

    /**
     * @param _maxWaitMillis 等候获取排它锁的最大等待时间，超过这个时间未获得锁则获取失败（单位：毫秒）
     * @return
     * @throws InterruptedException
     */
    boolean tryLock(long _maxWaitMillis) throws InterruptedException;

    void unlock();

}
