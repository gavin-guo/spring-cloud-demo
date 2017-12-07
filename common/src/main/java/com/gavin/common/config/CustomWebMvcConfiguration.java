package com.gavin.common.config;

import com.gavin.common.interceptor.EnhanceLogInterceptor;
import com.gavin.common.interceptor.PropagateUserIdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CustomWebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    @Qualifier("customRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new EnhanceLogInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new PropagateUserIdInterceptor()).addPathPatterns("/**");

        //        registry.addInterceptor(new UserInfoExtractorInterceptor(redisTemplate)).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}
