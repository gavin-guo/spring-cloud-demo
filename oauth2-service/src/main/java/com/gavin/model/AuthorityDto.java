package com.gavin.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AuthorityDto implements Serializable {

    private String authority;

}
