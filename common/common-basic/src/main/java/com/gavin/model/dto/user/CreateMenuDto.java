package com.gavin.model.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CreateMenuDto", description = "菜单创建信息")
public class CreateMenuDto implements Serializable {

    @JsonProperty("label")
    @ApiModelProperty(value = "显示标签", position = 1, required = true)
    private String label;

    @JsonProperty("value")
    @ApiModelProperty(value = "值", position = 2, required = true)
    private String value;

    @JsonProperty("parent_id")
    @ApiModelProperty(value = "父菜单ID", position = 3)
    private String parentId;

}
