package com.gavin.common.dto.security;

import lombok.Data;

@Data
public class CurrentUser {

    private String userId;

    private String nickName;

    private Byte grade;

}
