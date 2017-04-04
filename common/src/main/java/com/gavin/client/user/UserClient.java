package com.gavin.client.user;

import com.gavin.config.OAuth2FeignConfiguration;
import com.gavin.model.Response;
import com.gavin.model.dto.user.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "user-service", fallback = UserClientFallback.class, configuration = {OAuth2FeignConfiguration.class})
public interface UserClient {

    @RequestMapping(value = "/users/loading/{login_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Response<UserDto> loadUserByLoginName(@PathVariable("login_name") String _loginName);

}
