package com.gavin.gateway.filter;

import com.gavin.common.constants.RedisKeyConstants;
import com.gavin.common.constants.RequestHeaderConstants;
import com.gavin.common.dto.security.CurrentUser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
@Slf4j
public class TransformUserNameToIdFilter extends ZuulFilter {

    private static final String USER_ID = "user_id";

    @Autowired
    @Qualifier("customRedisTemplate")
    private RedisTemplate redisTemplate;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2Authentication) {
            String userName = (String) authentication.getPrincipal();

            Object obj = redisTemplate.opsForValue().get(String.format("%s:%s", RedisKeyConstants.USER_NAME_TO_INFO, userName));
            if (obj != null && obj instanceof CurrentUser) {
                CurrentUser currentUser = (CurrentUser) obj;

                RequestContext ctx = RequestContext.getCurrentContext();
                ctx.set(USER_ID, currentUser.getId());
                return true;
            }
        }

        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader(RequestHeaderConstants.X_USER_ID, (String) ctx.get(USER_ID));

        return null;
    }

}
