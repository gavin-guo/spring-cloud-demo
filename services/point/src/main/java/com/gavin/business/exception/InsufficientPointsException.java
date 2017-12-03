package com.gavin.business.exception;

import com.gavin.common.exception.CustomException;

/**
 * 可用积分数不足
 */
public class InsufficientPointsException extends CustomException {

    public InsufficientPointsException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "insufficient_points";
    }

}
