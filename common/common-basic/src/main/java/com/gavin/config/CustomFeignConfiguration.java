package com.gavin.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignConfiguration {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

//    @Bean
//    @ConditionalOnBean(OAuth2ClientContext.class)
//    @ConditionalOnClass({Feign.class})
//    @ConditionalOnProperty(value = "feign.oauth2.enabled", matchIfMissing = true)
//    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
//        return new OAuth2FeignRequestInterceptor(oauth2ClientContext);
//    }

}
