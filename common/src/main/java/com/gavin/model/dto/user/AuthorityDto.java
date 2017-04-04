package com.gavin.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AuthorityDto implements Serializable {

    @NotNull(message = "authority不能为空")
    private String authority;

}
