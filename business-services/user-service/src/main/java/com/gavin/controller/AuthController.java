package com.gavin.controller;

import com.gavin.exception.RecordNotFoundException;
import com.gavin.model.dto.user.UserDto;
import com.gavin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Slf4j
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/auth/{login_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiIgnore
    public UserDto loadUserByLoginName(
            @PathVariable("login_name") String _loginName) {
        try {
            UserDto userDto = userService.findUserByLoginName(_loginName);
            return userDto;
        } catch (RecordNotFoundException e) {
            UserDto userDto = new UserDto();
            userDto.setId(null);
            return userDto;
        }
    }

}
