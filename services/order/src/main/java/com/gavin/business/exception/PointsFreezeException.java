package com.gavin.business.exception;

import com.gavin.common.exception.CustomException;

/**
 * 无法冻结积分。
 */
public class PointsFreezeException extends CustomException {

    public PointsFreezeException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "freeze_points_failed";
    }

}
