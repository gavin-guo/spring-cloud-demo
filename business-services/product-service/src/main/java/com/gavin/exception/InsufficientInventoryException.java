package com.gavin.exception;

public class InsufficientInventoryException extends CustomException {

    public InsufficientInventoryException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "insufficient_inventory";
    }

}
