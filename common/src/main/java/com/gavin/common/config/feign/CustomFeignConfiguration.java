package com.gavin.common.config.feign;

import com.gavin.common.constants.RequestHeaderConstants;
import com.gavin.common.context.CustomHystrixContext;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
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
    public RequestInterceptor customRequestInterceptor() {
        return template -> {
            String userId = CustomHystrixContext.getInstance().getUserId();
            if (userId != null) {
                template.header(RequestHeaderConstants.X_USER_ID, userId);
            }
        };
    }

}
