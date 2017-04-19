package com.gavin.security.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationManagerDelegator implements AuthenticationManager {

    private AuthenticationManager delegate;

    public CustomAuthenticationManagerDelegator(AuthenticationManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("----------------------");
        Authentication result = null;
        result = delegate.authenticate(authentication);
        Object principal = result.getPrincipal();

        System.out.println("+++++++++++++++++");
        return result;
    }
}
