package com.gavin.common.dto.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
public class CustomUser extends User {

    private String id;

    private String loginName;

    private String nickName;

    private Byte grade;

    public CustomUser(String id,
                      String loginName,
                      String nickName,
                      String password,
                      boolean enabled,
                      boolean accountNonExpired,
                      boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities,
                      Byte grade) {
        super(loginName,
                password,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);

        this.id = id;
        this.loginName = loginName;
        this.nickName = nickName;
        this.grade = grade;
    }

}
