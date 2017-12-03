package com.gavin.common.exception;

public abstract class CustomException extends RuntimeException {

    public CustomException(String message, Throwable t) {
        super(message, t);
    }

    public CustomException(String message) {
        super(message);
    }

    public int getHttpCode() {
        return 200;
    }

    public abstract String getErrorCode();

}
