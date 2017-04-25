package com.gavin.exception;

public abstract class CustomException extends RuntimeException {

    protected CustomException(String msg, Throwable t) {
        super(msg, t);
    }

    protected CustomException(String msg) {
        super(msg);
    }

    public int getHttpCode() {
        return 200;
    }

    public abstract String getErrorCode();

}
