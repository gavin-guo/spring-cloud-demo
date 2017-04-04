package com.gavin.exception;

/**
 * 可用积分数不足
 */
public class PointNotEnoughException extends RuntimeException {

    public PointNotEnoughException(String message) {
        super(message);
    }

}
