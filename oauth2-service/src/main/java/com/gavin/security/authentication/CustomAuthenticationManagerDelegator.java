package com.gavin.security.authentication;

import com.gavin.security.model.CustomUser;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationManagerDelegator implements AuthenticationManager {

    private AuthenticationManager delegate;

    private RedisTemplate redisTemplate;

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
            HashOperations hashOperations = redisTemplate.opsForHash();
            hashOperations.put("login_user", customUser.getUsername(), customUser);
        }

        return result;
    }

}
