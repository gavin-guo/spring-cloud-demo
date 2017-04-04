package com.gavin.exception;

public class LoginNameExistingException extends RuntimeException {

    public LoginNameExistingException(String message) {
        super(message);
    }

}
