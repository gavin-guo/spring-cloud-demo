package com.gavin.config;

import com.gavin.interceptor.RequestLogEnhancerInterceptor;
import com.gavin.interceptor.UserIdPropagatorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnMissingClass("com.gavin.config.AuthorizationServerConfiguration")
public class CustomWebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("customRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserIdPropagatorInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new RequestLogEnhancerInterceptor(discoveryClient)).addPathPatterns("/**");
        //        registry.addInterceptor(new UserInfoExtractorInterceptor(redisTemplate)).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}
