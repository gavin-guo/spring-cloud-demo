package com.gavin.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "CreateUserDto", description = "用户创建信息")
public class CreateUserDto implements Serializable {

    @JsonProperty("login_name")
    @NotNull(message = "login_name不能为空")
    @ApiModelProperty(value = "登录名", position = 1, required = true)
    private String loginName;

    @JsonProperty("password")
    @NotNull(message = "password不能为空")
    @ApiModelProperty(value = "密码", position = 2, required = true)
    private String password;

    @JsonProperty("nick_name")
    @NotNull(message = "nick_name不能为空")
    @ApiModelProperty(value = "昵称", position = 3, required = true)
    private String nickName;

    @JsonProperty("email")
    @NotNull(message = "email不能为空")
    @ApiModelProperty(value = "联系邮箱", position = 4, required = true)
    private String email;

    @JsonProperty("phone")
    @NotNull(message = "phone不能为空")
    @ApiModelProperty(value = "联系电话", position = 5, required = true)
    private String phone;

}
