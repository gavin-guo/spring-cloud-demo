package com.gavin.config;

import com.gavin.constants.RequestAttributeConstants;
import com.gavin.model.dto.security.CurrentUser;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Configuration
@Slf4j
public class CustomFeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return template -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            Object obj = requestAttributes.getAttribute(RequestAttributeConstants.CURRENT_USER, RequestAttributes.SCOPE_REQUEST);
            if (obj != null) {
                CurrentUser currentUser = (CurrentUser) obj;
                template.header("x-user-id", currentUser.getUserId());
            }
        };
    }

}
