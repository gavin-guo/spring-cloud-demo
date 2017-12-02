package com.gavin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gavin.constants.ResponseCodeConstants;
import com.gavin.dto.common.CustomResponseBody;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType(ContentType.APPLICATION_JSON.toString());

        CustomResponseBody responseBody = new CustomResponseBody(ResponseCodeConstants.UNAUTHORIZED, e.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

}
