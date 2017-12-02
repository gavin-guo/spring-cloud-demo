package com.gavin.service;

import com.gavin.client.user.UserClient;
import com.gavin.constants.ResponseCodeConstants;
import com.gavin.dto.common.CustomResponseBody;
import com.gavin.dto.security.CustomUser;
import com.gavin.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Autowired
    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String _username) throws UsernameNotFoundException {

        CustomResponseBody<UserDto> response = userClient.loadUserByLoginName(_username);
        if (!response.getCode().equals(ResponseCodeConstants.OK)) {
            throw new UsernameNotFoundException(_username);
        }

        UserDto userDto = response.getRecord();

        List<GrantedAuthority> authorities = new ArrayList<>();
        userDto.getAuthorities().forEach(
                authority -> authorities.add(new SimpleGrantedAuthority(authority.getAuthority()))
        );

        CustomUser customUser = new CustomUser(
                userDto.getId(),
                userDto.getLoginName(),
                userDto.getPassword(),
                true,
                true,
                true,
                true,
                authorities,
                userDto.getGrade());

        return customUser;
    }

}
