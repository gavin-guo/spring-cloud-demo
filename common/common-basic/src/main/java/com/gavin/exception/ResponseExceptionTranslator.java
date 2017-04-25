package com.gavin.exception;

import com.gavin.model.StandardResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

public class ResponseExceptionTranslator {

    public static ResponseEntity<StandardResponseBody> translate(Exception e) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");

        Map<String, String> map = new HashMap<>();

        StandardResponseBody body = new StandardResponseBody();

        ResponseEntity<StandardResponseBody> response;
        HttpStatus httpStatus;
        if (e instanceof CustomException) {
            CustomException ce = (CustomException) e;
            body.setCode(ce.getErrorCode());
            body.setMessage(ce.getMessage());
            httpStatus = HttpStatus.valueOf(ce.getHttpCode());
        } else if (e instanceof HttpMessageNotReadableException || e instanceof MethodArgumentNotValidException) {
            body.setCode("bad_request");
            body.setMessage(e.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        } else {
            body.setCode("server_error");
            body.setMessage(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response = new ResponseEntity<>(body, headers, httpStatus);
        return response;
    }

}
