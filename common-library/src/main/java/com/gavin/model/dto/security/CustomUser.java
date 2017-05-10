package com.gavin.model.dto.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
public class CustomUser extends User {

    private String nickName;

    private Byte grade;

    public CustomUser(String userId,
                      String nickName,
                      String password,
                      boolean enabled,
                      boolean accountNonExpired,
                      boolean credentialsNonExpired,
                      boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities,
                      Byte grade) {
        super(userId,
                password,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);

        this.nickName = nickName;
        this.grade = grade;
    }

}
