package com.gavin.common.dto.user;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AuthorityDto implements Serializable {

    @NotNull(message = "'authority' should not be null")
    private String authority;

}
