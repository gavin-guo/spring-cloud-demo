package com.gavin.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.exception.LoginNameExistingException;
import com.gavin.model.Response;
import com.gavin.model.dto.user.AuthorityDto;
import com.gavin.model.dto.user.CreateUserDto;
import com.gavin.model.vo.user.UserVo;
import com.gavin.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/users", description = "用户相关API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "创建用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<UserVo> createUser(
            @ApiParam(name = "user", value = "要创建的用户信息", required = true) @Valid @RequestBody CreateUserDto _user) {
        Response<UserVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        try {
            UserVo userVo = userService.createUser(_user);
            log.info("用户{}创建成功。", userVo.getId());

            response.setContents(userVo);
            return response;
        } catch (LoginNameExistingException e) {
            log.warn("用户{}已经存在。", _user.getLoginName());
            response.setCode(ResponseCodeConstants.LOGIN_NAME_ALREADY_EXIST);
            return response;
        }
    }

    @RequestMapping(value = "/users/activation/{user_id}", method = RequestMethod.GET)
    @ApiOperation(value = "激活用户")
    public Response activateUser(
            @ApiParam(name = "user_id", value = "要激活的用户ID", required = true) @Valid @PathVariable("user_id") String _userId) {
        Response response = new Response<>(ResponseCodeConstants.SUCCESS);

        userService.activateUser(_userId);
        return response;
    }

    @RequestMapping(value = "/users/{user_id}/grant", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "授予用户权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response grantAuthorities(
            @ApiParam(name = "user_id", value = "对象用户", required = true) @PathVariable("user_id") String _userId,
            @ApiParam(name = "authorities", value = "要赋予的权限", required = true) @Valid @RequestBody List<AuthorityDto> _authorities) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);
        userService.updateAuthorities(_userId, _authorities);
        return response;
    }

}
