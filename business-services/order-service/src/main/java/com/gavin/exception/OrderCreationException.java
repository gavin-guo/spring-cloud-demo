package com.gavin.exception;

/**
 * 无法创建订单。
 */
public class OrderCreationException extends RuntimeException {

    public OrderCreationException(Throwable cause) {
        super(cause);
    }

}
