package com.gavin.model;

import lombok.Data;

@Data
public class UserCreatedMailDto {

    private String userId;

    private String loginName;

    private String nickName;

    private String email;

    private String phone;

}
