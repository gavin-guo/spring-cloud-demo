package com.gavin.business.exception;

import com.gavin.common.exception.CustomException;

/**
 * 无法锁定库存。
 */
public class ProductsReserveException extends CustomException {

    public ProductsReserveException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "reserve_failed";
    }
}
