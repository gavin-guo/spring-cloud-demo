package com.gavin.common.controller;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponse;
import com.gavin.common.dto.common.PageResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@RestControllerAdvice("com.gavin.business.controller")
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
            CustomResponse<Object> responseBody = new CustomResponse<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            return responseBody;
        } else if (body instanceof List) {
            CustomResponse<Object> responseBody = new CustomResponse<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setData(body);
            return responseBody;
        } else if (body instanceof PageResult) {
            PageResult pageResult = (PageResult) body;
            CustomResponse<Object> responseBody = new CustomResponse<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setPageResult(pageResult);
            return responseBody;
        } else {
            CustomResponse<Object> responseBody = new CustomResponse<>();
            responseBody.setCode(ResponseCodeConstants.OK);
            responseBody.setData(body);
            return responseBody;
        }
    }

}
