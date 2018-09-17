package com.gavin.common.advice;

import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponseBody;
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
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // already processed by ExceptionHandler
        return (returnType.getContainingClass() != CustomExceptionAdvice.class)
                && (!returnType.getMethod().getName().equals("ping"));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        CustomResponseBody<Object> responseBody = new CustomResponseBody<>(ResponseCodeConstants.OK);

        if (body == null) {
            // when return type of controller method is void
        } else if (body instanceof List) {
            responseBody.setList((List) body);
        } else if (body instanceof PageResult) {
            responseBody.setPageResult((PageResult) body);
        } else {
            responseBody.setData(body);
        }

        return responseBody;
    }

}
