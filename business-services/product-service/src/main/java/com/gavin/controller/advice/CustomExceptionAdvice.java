package com.gavin.controller.advice;

import com.gavin.exception.CustomException;
import com.gavin.model.StandardResponseBody;
import com.gavin.utility.ResponseExceptionTranslator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice("com.gavin.controller")
@Slf4j
public class CustomExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<StandardResponseBody> handleException(Exception e) throws Exception {
        log.info(String.format("handling error: %s, %s.", e.getClass().getSimpleName(), e.getMessage()));
        return ResponseExceptionTranslator.translate(e);
    }

}
