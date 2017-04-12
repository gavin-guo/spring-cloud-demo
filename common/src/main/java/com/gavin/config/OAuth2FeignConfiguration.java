//package com.gavin.config;
//
//import com.gavin.feign.OAuth2FeignRequestInterceptor;
//import feign.Feign;
//import feign.RequestInterceptor;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//
//@Configuration
//@ConditionalOnClass({Feign.class})
//@ConditionalOnProperty(value = "feign.oauth2.enabled", matchIfMissing = true)
//public class OAuth2FeignConfiguration {
//
//    @Bean
//    @ConditionalOnBean(OAuth2ClientContext.class)
//    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext) {
//        return new OAuth2FeignRequestInterceptor(oauth2ClientContext);
//    }
//
//}
