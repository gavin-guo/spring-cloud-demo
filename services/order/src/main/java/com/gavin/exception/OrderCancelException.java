package com.gavin.exception;

/**
 * 无法撤销订单。
 */
public class OrderCancelException extends CustomException {

    public OrderCancelException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "cannot_cancel_order";
    }

}
