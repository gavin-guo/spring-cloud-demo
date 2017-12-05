package com.gavin.business.exception;

import com.gavin.common.exception.CustomException;

public class UserExistingException extends CustomException {

    public UserExistingException(String message) {
        super(message);
    }

    @Override
    public String getErrorCode() {
        return "user_exists";
    }

}
