package com.gavin.base;

/**
 * 消息订阅者的接口定义。
 *
 * @param <T>
 */
public interface MessageConsumer<T> {

    /**
     * 接收到消息被调用的方法。
     *
     * @param payload
     */
    void receiveMessage(T payload);

}
