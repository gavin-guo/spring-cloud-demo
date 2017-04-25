package com.gavin.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "code",
        "details",
        "contents",
        "page"})
@Data
@ApiModel(value = "StandardResponseBody", description = "返回结果")
public class StandardResponseBody<T> implements Serializable {

    @JsonProperty("code")
    @ApiModelProperty(value = "执行结果CODE", position = 1)
    private String code;

    @JsonProperty("message")
    @ApiModelProperty(value = "详细信息", position = 2)
    private String message;

    @JsonProperty("data")
    @ApiModelProperty(value = "内容", position = 3)
    private T data;

    @JsonProperty("page")
    @ApiModelProperty(value = "分页参数", position = 4)
    private PageArgument page;

    public StandardResponseBody() {
    }

}
