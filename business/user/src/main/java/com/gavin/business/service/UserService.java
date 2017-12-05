package com.gavin.business.service;

import com.gavin.common.dto.user.CreateUserDto;
import com.gavin.common.dto.user.UserDto;

public interface UserService {

    /**
     * 创建用户
     *
     * @param _user
     * @return
     */
    UserDto createUser(CreateUserDto _user);

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
    void updateAuthorities(String _userId, String[] _authorities);

}
