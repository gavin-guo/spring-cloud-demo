package com.gavin.controller.advice;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.exception.ProductsReserveException;
import com.gavin.exception.RemoteCallException;
import com.gavin.exception.RemoteProcessException;
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

    @ExceptionHandler({
            RemoteCallException.class,
            RemoteProcessException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleRemoteException(Exception e) {
        log.error(e.getMessage(), e);
        Response response = new Response(ResponseCodeConstants.REMOTE_CALL_FAILED);
        response.setDetails(e.getMessage());
        return response;
    }

    @ExceptionHandler(ProductsReserveException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleProductsReserveException(Exception e) {
        log.error(e.getMessage(), e);
        Response response = new Response(ResponseCodeConstants.RESERVE_PRODUCT_FAILED);
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
