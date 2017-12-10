package com.gavin.common.dto.security;

import lombok.Data;

import java.io.Serializable;

@Data
public class CurrentUser implements Serializable {

    private String id;

    String userName;

    private String nickName;

    private Byte grade;

}
