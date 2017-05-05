package com.gavin.model.dto.security;

import lombok.Data;

@Data
public class CurrentUser {

    private String userId;

    private String userName;

    private Byte grade;

    private boolean admin;

}
