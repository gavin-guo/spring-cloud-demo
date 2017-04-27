package com.gavin.service;

import com.gavin.model.dto.user.AuthorityDto;
import com.gavin.model.dto.user.CreateUserDto;
import com.gavin.model.dto.user.UserDto;
import com.gavin.model.vo.user.UserVo;

import java.util.List;

public interface UserService {

    /**
     * 创建用户
     *
     * @param _user
     * @return
     */
    UserVo createUser(CreateUserDto _user);

    /**
     * 激活用户
     *
     * @param _userId
     */
    void activateUser(String _userId);

    /**
     * 根据登录名查找用户
     *
     * @param _loginName
     * @return
     */
    UserDto findUserByLoginName(String _loginName);

    /**
     * 更新用户权限
     *
     * @param _userId
     * @param _authorities
     */
    void updateAuthorities(String _userId, List<AuthorityDto> _authorities);

/*    *//**
     * 发布用户帐号创建成功的事件
     *
     * @param _event
     * @return
     *//*
    boolean publishUserCreatedEvent(UserCreatedEvent _event);

    *//**
     * 发布用户帐号已激活的事件
     *
     * @param _event
     * @return
     *//*
    boolean publishUserActivatedEvent(UserActivatedEvent _event);*/

}
