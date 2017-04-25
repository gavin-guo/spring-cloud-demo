package com.gavin.controller.advice;

import com.gavin.exception.ResponseExceptionTranslator;
import com.gavin.model.StandardResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.gavin.controller")
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponseBody> handleException(Exception e) throws Exception {
        log.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        return ResponseExceptionTranslator.translate(e);
    }

}
