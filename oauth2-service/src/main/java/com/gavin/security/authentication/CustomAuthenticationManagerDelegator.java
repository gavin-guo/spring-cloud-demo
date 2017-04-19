package com.gavin.security.authentication;

import com.gavin.security.model.CustomUser;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.concurrent.TimeUnit;

public class CustomAuthenticationManagerDelegator implements AuthenticationManager {

    private AuthenticationManager delegate;

    private RedisTemplate<String, Object> redisTemplate;

    public CustomAuthenticationManagerDelegator(AuthenticationManager delegate, RedisTemplate redisTemplate) {
        this.delegate = delegate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = delegate.authenticate(authentication);
        Object principal = result.getPrincipal();

        if (principal instanceof CustomUser) {
            CustomUser customUser = (CustomUser) principal;

            String loginName = String.format("login_user:%s", customUser.getUsername());
            BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(loginName);
            boundValueOperations.set(customUser);
            boundValueOperations.expire(1, TimeUnit.HOURS);
        }

        return result;
    }

}
