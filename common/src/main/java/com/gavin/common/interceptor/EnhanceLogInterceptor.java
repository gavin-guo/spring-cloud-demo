package com.gavin.common.interceptor;

import com.gavin.common.constants.RequestHeaderConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * added to registry on {@link com.gavin.common.config.CustomWebMvcConfiguration}
 */
@Slf4j
@AllArgsConstructor
public class EnhanceLogInterceptor implements HandlerInterceptor {

    private static final String X_USER_ID = "x_user_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader(RequestHeaderConstants.X_USER_ID);

        if (userId != null) {
            MDC.put(X_USER_ID, userId);
        }

        log.info("{}?{} {}", request.getRequestURI(), request.getQueryString(), request.getMethod());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        MDC.remove(X_USER_ID);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }

}
