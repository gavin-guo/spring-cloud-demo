package com.gavin.common.config;

import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CustomFeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    @ConditionalOnMissingClass("com.gavin.common.config.AuthorizationServerConfiguration")
    public RequestInterceptor customRequestInterceptor() {
        return template -> {
//            String userId = CustomHystrixContext.getInstance().getUserId();
//
//            if (userId != null) {
//                log.debug("get userId from CustomHystrixContext: {}", userId);
//                template.header(RequestHeaderConstants.X_USER_ID, userId);
//            }

        };
    }

}
