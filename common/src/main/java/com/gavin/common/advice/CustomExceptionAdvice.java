package com.gavin.common.advice;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponseBody;
import com.gavin.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * see {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler}
 */
@RestControllerAdvice("com.gavin.business.controller")
@Slf4j
public class CustomExceptionAdvice {

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<CustomResponseBody> handleCustomException(Exception e) {
        log.info(String.format("handling exception: %s.", e.getClass().getName()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");

        CustomResponseBody responseBody = new CustomResponseBody();
        ResponseEntity<CustomResponseBody> responseEntity;
        HttpStatus httpStatus;

        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;

            httpStatus = HttpStatus.valueOf(customException.getHttpCode());

            responseBody.setCode(customException.getErrorCode());
            responseBody.setMessage(customException.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;

            httpStatus = HttpStatus.BAD_REQUEST;

            String message = validException.getBindingResult().getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            responseBody.setCode(ResponseCodeConstants.BAD_REQUEST);
            responseBody.setMessage(message);
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            responseBody.setCode(ResponseCodeConstants.INTERNAL_SERVER_ERROR);
            responseBody.setMessage(e.getMessage());
        }

        responseEntity = new ResponseEntity<>(responseBody, headers, httpStatus);
        return responseEntity;
    }

}
