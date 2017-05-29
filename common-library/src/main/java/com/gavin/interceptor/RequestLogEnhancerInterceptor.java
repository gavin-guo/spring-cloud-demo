package com.gavin.interceptor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@AllArgsConstructor
public class RequestLogEnhancerInterceptor implements HandlerInterceptor {

    private static final String HOST = "host";

    private static final String PORT = "port";

    private static final String SERVICE = "service";

    DiscoveryClient discoveryClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ServiceInstance serviceInstance = discoveryClient.getLocalServiceInstance();
        MDC.put(HOST, serviceInstance.getHost());
        MDC.put(PORT, String.valueOf(serviceInstance.getPort()));
        MDC.put(SERVICE, serviceInstance.getServiceId());
        log.info("{} {}", request.getRequestURI(), request.getMethod());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        MDC.remove(HOST);
        MDC.remove(PORT);
        MDC.remove(SERVICE);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
