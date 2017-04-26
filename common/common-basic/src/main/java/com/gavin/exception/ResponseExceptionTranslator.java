package com.gavin.exception;

import com.gavin.model.response.ExecutionResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseExceptionTranslator {

    public static ResponseEntity<ExecutionResponseBody> translate(Exception e) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");

        ExecutionResponseBody body = new ExecutionResponseBody();

        ResponseEntity<ExecutionResponseBody> response;
        HttpStatus httpStatus;
        if (e instanceof CustomException) {
            CustomException ce = (CustomException) e;
            body.setCode(ce.getErrorCode());
            body.setMessage(ce.getMessage());
            httpStatus = HttpStatus.valueOf(ce.getHttpCode());
        } else {
            body.setCode("server_error");
            body.setMessage(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response = new ResponseEntity<>(body, headers, httpStatus);
        return response;
    }

}
