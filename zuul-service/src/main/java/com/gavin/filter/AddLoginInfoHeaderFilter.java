package com.gavin.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
public class AddLoginInfoHeaderFilter extends ZuulFilter {

    private static final String LOGIN_INFO = "LOGIN_INFO";

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof OAuth2Authentication) {
                Object details = authentication.getDetails();
                if (details instanceof OAuth2AuthenticationDetails) {
                    OAuth2AuthenticationDetails authenticationDetails = (OAuth2AuthenticationDetails) details;

                    String loginUser = (String) authentication.getPrincipal();
                    String token = authenticationDetails.getTokenValue();

                    String loginInfo = String.format("%s:%s", loginUser, token);

                    String encodedInfo;
                    try {
                        encodedInfo = new String(Base64.encode(loginInfo.getBytes("UTF-8")));
                    } catch (UnsupportedEncodingException e) {
                        throw new IllegalStateException("Could not convert String");
                    }

                    RequestContext ctx = RequestContext.getCurrentContext();
                    ctx.set(LOGIN_INFO, encodedInfo);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error("AddLoginInfoHeaderFilter can not work.", e);
            return false;
        }
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("login-info", (String) ctx.get(LOGIN_INFO));
        return null;
    }

}
