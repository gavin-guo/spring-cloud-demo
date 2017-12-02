package com.gavin.common.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.dto.common.CustomResponseBody;
import com.gavin.dto.common.PageResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice("com.gavin.controller")
public class CustomResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // already processed by ExceptionHandler
        if (returnType.getContainingClass() == CustomExceptionAdvice.class) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            // when return type of controller method is void
            CustomResponseBody<Object> responseBody = new CustomResponseBody<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            return responseBody;
        } else if (body instanceof List) {
            CustomResponseBody<Object> responseBody = new CustomResponseBody<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setData(body);
            return responseBody;
        } else if (body instanceof PageResult) {
            PageResult pageResult = (PageResult) body;
            CustomResponseBody<Object> responseBody = new CustomResponseBody<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setPageResult(pageResult);
            return responseBody;
        } else {
            CustomResponseBody<Object> responseBody = new CustomResponseBody<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setData(body);
            return responseBody;
        }
    }

}
