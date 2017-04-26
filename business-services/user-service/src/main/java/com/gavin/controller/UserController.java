package com.gavin.controller;

import com.gavin.model.dto.user.AuthorityDto;
import com.gavin.model.dto.user.CreateUserDto;
import com.gavin.model.vo.user.UserVo;
import com.gavin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/users", description = "用户相关API")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ApiOperation(value = "创建用户")
    public UserVo createUser(
            @ApiParam(name = "user", value = "要创建的用户信息", required = true) @Valid @RequestBody CreateUserDto _user) {
        UserVo userVo = userService.createUser(_user);
        log.info("create user {} successfully.", userVo.getId());
        return userVo;
    }

    @RequestMapping(value = "/users/activation", method = RequestMethod.GET)
    @ApiOperation(value = "激活用户")
    public void activateUser(
            @ApiParam(name = "user_id", value = "要激活的用户ID", required = true) @Valid @RequestParam("user_id") String _userId) {
        userService.activateUser(_userId);
    }

    @RequestMapping(value = "/users/grant", method = RequestMethod.PUT)
    @ApiOperation(value = "授予用户权限")
    public void grantAuthorities(
            @ApiParam(name = "user_id", value = "对象用户", required = true) @RequestParam("user_id") String _userId,
            @ApiParam(name = "authorities", value = "要赋予的权限", required = true) @Valid @RequestBody List<AuthorityDto> _authorities) {
        userService.updateAuthorities(_userId, _authorities);
    }

}
