package com.gavin.filter;

import com.gavin.constants.RequestHeaderConstants;
import com.gavin.dto.security.CustomUser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
@Slf4j
public class AddUserToHeaderFilter extends ZuulFilter {

    private static final String USER_ID = "USER_ID";

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 8;
    }

    @Override
    public boolean shouldFilter() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal != null && principal instanceof CustomUser) {
            CustomUser customUser = (CustomUser) principal;

            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.set(USER_ID, customUser.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(RequestHeaderConstants.X_USER_ID, (String) ctx.get(USER_ID));

        return null;
    }

}