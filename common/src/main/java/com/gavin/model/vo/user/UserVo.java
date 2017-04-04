package com.gavin.model.vo.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "loginName",
        "nickName",
        "email",
        "phone"})
@Data
public class UserVo implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("login_name")
    private String loginName;

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

}
