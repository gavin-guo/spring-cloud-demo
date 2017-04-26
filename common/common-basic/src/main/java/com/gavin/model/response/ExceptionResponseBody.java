package com.gavin.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "code",
        "message"})
@Data
@NoArgsConstructor
@ApiModel(value = "ExecutionResponse", description = "异常信息")
public class ExceptionResponseBody<T> implements Serializable {

    @JsonProperty("error_code")
    @ApiModelProperty(value = "异常结果CODE", position = 1)
    private String code;

    @JsonProperty("error_message")
    @ApiModelProperty(value = "异常的详细信息", position = 2)
    private String message;

}
