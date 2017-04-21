package com.gavin.client;

import com.gavin.model.dto.user.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", fallback = UserClientFallback.class)
public interface UserClient {

    @RequestMapping(value = "/user/loading", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserDto loadUserByLoginName(@RequestParam("login_name") String _loginName);

}
