package com.gavin.security.service;

import com.gavin.client.UserClient;
import com.gavin.model.dto.user.UserDto;
import com.gavin.security.model.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Autowired
    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String _username) throws UsernameNotFoundException {

        UserDto userDto = userClient.loadUserByLoginName(_username);

        if (userDto == null || userDto.getId() == null) {
            throw new UsernameNotFoundException(_username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        userDto.getAuthorities().forEach(
                authority -> authorities.add(new SimpleGrantedAuthority(authority.getAuthority()))
        );

        CustomUser customUser = new CustomUser(
                userDto.getLoginName(),
                userDto.getPassword(),
                true,
                true,
                true,
                true,
                authorities,
                userDto.getId(),
                userDto.getGrade(),
                userDto.getAdminFlag());

        return customUser;
    }

}
