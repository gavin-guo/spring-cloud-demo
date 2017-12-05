package com.gavin.business.exception;

import com.gavin.common.exception.CustomException;

public class InsufficientInventoryException extends CustomException {

    public InsufficientInventoryException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "insufficient_inventory";
    }

}
