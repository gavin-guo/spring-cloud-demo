package com.gavin.exception;

/**
 * 服务端产生的异常
 */
public class ServerErrorException extends CustomException {

    public ServerErrorException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return 500;
    }

    @Override
    public String getErrorCode() {
        return "server_error";
    }

}
