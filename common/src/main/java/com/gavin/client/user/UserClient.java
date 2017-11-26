package com.gavin.client.user;

import com.gavin.dto.common.CustomResponseBody;
import com.gavin.dto.user.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user", fallback = UserClientFallback.class)
public interface UserClient {

    @RequestMapping(value = "/users/loading", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CustomResponseBody<UserDto> loadUserByLoginName(@RequestParam("login_name") String _loginName);

}
