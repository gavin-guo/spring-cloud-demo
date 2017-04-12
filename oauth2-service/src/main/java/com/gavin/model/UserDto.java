package com.gavin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("login_name")
    private String loginName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("grade")
    private Byte grade;

    @JsonProperty("admin_flag")
    private Boolean adminFlag;

    @JsonProperty("authorities")
    private List<AuthorityDto> authorities;

}
