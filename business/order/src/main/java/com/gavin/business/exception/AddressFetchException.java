package com.gavin.business.exception;

import com.gavin.common.exception.CustomException;

/**
 * 无法获得详细地址。
 */
public class AddressFetchException extends CustomException {

    public AddressFetchException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "cannot_fetch_address";
    }

}
