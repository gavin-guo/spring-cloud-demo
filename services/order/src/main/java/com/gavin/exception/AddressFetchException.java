package com.gavin.exception;

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
