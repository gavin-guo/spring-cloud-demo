package com.gavin.interceptor;

import com.gavin.constants.RequestAttributeConstants;
import com.gavin.model.dto.security.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@AllArgsConstructor
public class ExtractLoginInfoInterceptor implements HandlerInterceptor {

    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("login-info");

        if (header == null) {
            return true;
        }

        byte[] base64Token = header.getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode login information.", e);
            return false;
        }

        String token = new String(decoded, "UTF-8");
        String[] loginInfo = token.split(":");

        if (loginInfo.length < 2) {
            log.error(String.format("Invalid login information. %s", token));
            return false;
        }

        String loginUser = String.format("login_user:%s", loginInfo[0]);
        String accessToken = loginInfo[1];

        Object obj = redisTemplate.opsForHash().get(loginUser, accessToken);
        if (obj != null) {
            CurrentUser currentUser = (CurrentUser) obj;
            request.setAttribute(RequestAttributeConstants.CURRENT_USER, currentUser);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
