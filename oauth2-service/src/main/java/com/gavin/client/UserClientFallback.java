package com.gavin.client;


import com.gavin.model.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserDto loadUserByLoginName(String _loginName) {
        return null;
    }

}
