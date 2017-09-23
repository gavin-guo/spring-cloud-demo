package com.gavin.exception;

public class UserExistingException extends CustomException {

    public UserExistingException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "user_exists";
    }

}
