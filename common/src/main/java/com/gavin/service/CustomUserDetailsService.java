package com.gavin.service;

import com.gavin.client.user.UserClient;
import com.gavin.constants.CacheNameConstants;
import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.Response;
import com.gavin.model.dto.security.CustomUser;
import com.gavin.model.dto.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Autowired
    public CustomUserDetailsService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    @Cacheable(cacheNames = CacheNameConstants.CACHE_CURRENT_USER, key = "#_username")
    public UserDetails loadUserByUsername(String _username) throws UsernameNotFoundException {

        Response<UserDto> response = userClient.loadUserByLoginName(_username);

        if (!ResponseCodeConstants.SUCCESS.equals(response.getCode())) {
            throw new UsernameNotFoundException(_username);
        }
        UserDto userDto = Optional.ofNullable(response.getContents())
                .orElseThrow(() -> new UsernameNotFoundException(_username));

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
