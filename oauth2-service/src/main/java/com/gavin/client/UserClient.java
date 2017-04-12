package com.gavin.client;


import com.gavin.model.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "user-service", fallback = UserClientFallback.class)
public interface UserClient {

    @RequestMapping(value = "/auth/{login_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    UserDto loadUserByLoginName(@PathVariable("login_name") String _loginName);

}
