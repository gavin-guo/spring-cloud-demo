package com.gavin.business.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gavin.common.constants.ResponseCodeConstants;
import com.gavin.common.dto.common.CustomResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        response.setContentType(ContentType.APPLICATION_JSON.toString());

        CustomResponse responseBody = new CustomResponse(ResponseCodeConstants.UNAUTHORIZED, exception.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

}
