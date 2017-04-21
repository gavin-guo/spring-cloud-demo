package com.gavin.base;

/**
 * 消息发布者的接口定义。
 *
 * @param <T>
 */
public interface MessageProducer<T> {

    /**
     * 发送消息到消息中间件。
     *
     * @param _event
     * @return
     */
    boolean sendMessage(T _event);

}
