package com.gavin.exception;

public class StocksNotEnoughException extends RuntimeException {

    public StocksNotEnoughException(String message) {
        super(message);
    }

}
