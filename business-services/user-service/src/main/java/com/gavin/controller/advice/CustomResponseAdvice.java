package com.gavin.controller.advice;


import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.response.ExecutionResponseBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice("com.gavin.controller")
public class CustomResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // already processed by ExceptionHandler
        if (body instanceof ExecutionResponseBody) {
            return body;
        }

        ExecutionResponseBody<Object> responseBody = new ExecutionResponseBody<>();
        responseBody.setCode(ResponseCodeConstants.OK);
        return responseBody;
    }

}
