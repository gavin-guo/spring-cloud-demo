package com.gavin.common.advice;

import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice("com.gavin.business.controller")
@Slf4j
public class CustomExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponseBody> handleException(Exception e) {
        log.info(String.format("handling exception: %s, %s.", e.getClass().getSimpleName(), e.getMessage()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");

        CustomResponseBody responseBody = new CustomResponseBody();

        ResponseEntity<CustomResponseBody> responseEntity;
        HttpStatus httpStatus;
        if (e instanceof CustomException) {
            CustomException ce = (CustomException) e;
            responseBody.setCode(ce.getErrorCode());
            responseBody.setMessage(ce.getMessage());
            httpStatus = HttpStatus.valueOf(ce.getHttpCode());
        } else {
            responseBody.setCode("server_error");
            responseBody.setMessage(e.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        responseEntity = new ResponseEntity<>(responseBody, headers, httpStatus);
        return responseEntity;
    }

}
