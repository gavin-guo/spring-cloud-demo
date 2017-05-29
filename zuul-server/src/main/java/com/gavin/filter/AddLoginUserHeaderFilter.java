package com.gavin.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
@Slf4j
public class AddLoginUserHeaderFilter extends ZuulFilter {

    private static final String LOGIN_USER_ID = "LOGIN_USER_ID";

    @Override
    public String filterType() {
        return PRE_TYPE;
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

                    String userId = (String) authentication.getPrincipal();

//                    String token = authenticationDetails.getTokenValue();
//                    String loginInfo = String.format("%s:%s", loginUser, token);
//                    String encoded;
//                    try {
//                        encoded = new String(Base64.encode(loginInfo.getBytes("UTF-8")));
//                    } catch (UnsupportedEncodingException e) {
//                        throw new IllegalStateException("Could not convert String");
//                    }

                    RequestContext ctx = RequestContext.getCurrentContext();
                    ctx.set(LOGIN_USER_ID, userId);
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
        ctx.addZuulRequestHeader("x-user-id", (String) ctx.get(LOGIN_USER_ID));
        return null;
    }

}
