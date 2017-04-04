package com.gavin.controller.advice;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.exception.StocksNotEnoughException;
import com.gavin.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.gavin.controller")
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleArgumentException(Exception e) {
        log.error(e.getMessage(), e);
        Response response = new Response(ResponseCodeConstants.BAD_REQUEST);
        response.setDetails("bad request.");
        return response;
    }

    @ExceptionHandler(StocksNotEnoughException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleStocksNotEnoughException(Exception e) {
        log.error(e.getMessage(), e);
        Response response = new Response(ResponseCodeConstants.STOCKS_NOT_ENOUGH);
        response.setDetails("insufficient inventory.");
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleException(Exception e) {
        log.error(e.getMessage(), e);
        Response response = new Response(ResponseCodeConstants.INTERNAL_EXCEPTION);
        response.setDetails("internal server error.");
        return response;
    }

}
