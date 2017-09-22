package com.gavin.lock;

public interface DistributedLockCallback<T> {

    /**
     * 获取排它锁成功后被调用的方法
     *
     * @return
     * @throws InterruptedException
     */
    T onLockingSucceeded() throws InterruptedException;

    /**
     * 等待获取排它锁一直到超时后被调用的方法
     *
     * @return
     * @throws InterruptedException
     */
    T onLockingFailed() throws InterruptedException;

}
