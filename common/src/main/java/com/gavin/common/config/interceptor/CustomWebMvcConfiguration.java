package com.gavin.common.config.interceptor;

import com.gavin.common.interceptor.EnhanceLogInterceptor;
import com.gavin.common.interceptor.PropagateUserIdInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CustomWebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new EnhanceLogInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new PropagateUserIdInterceptor()).addPathPatterns("/**");

        super.addInterceptors(registry);
    }

}
