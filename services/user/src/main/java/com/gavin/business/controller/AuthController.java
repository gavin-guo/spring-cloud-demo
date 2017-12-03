package com.gavin.business.controller;

import com.gavin.dto.user.UserDto;
import com.gavin.business.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users/loading", method = RequestMethod.GET)
    @ApiIgnore
    public UserDto loadUserByLoginName(@RequestParam("login_name") String _loginName) {
        return userService.findUserByLoginName(_loginName);
    }

}
