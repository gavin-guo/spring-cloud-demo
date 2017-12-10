package com.gavin.common.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "loginName",
        "nickName",
        "email",
        "mobile_number"})
@Data
public class UserDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("login_name")
    private String loginName;

    @JsonProperty("password")
    private String password;

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    @JsonProperty("grade")
    private Byte grade;

    @JsonProperty("authorities")
    private List<AuthorityDto> authorities;

}
