package com.gavin.exception;

/**
 * 微服务端执行产生的异常
 */
public class RemoteProcessException extends RuntimeException {

    public RemoteProcessException(String message) {
        super(message);
    }
}
