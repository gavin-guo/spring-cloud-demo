package com.gavin.dto.common;

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
        "resultCode",
        "errorMessage",
        "totalRecords",
        "totalPages",
        "currentPage",
        "contents"})
@Data
@NoArgsConstructor
@ApiModel(value = "Response", description = "返回结果")
public class CustomResponseBody<T> implements Serializable {

    @JsonProperty("result_code")
    @ApiModelProperty(value = "执行结果CODE", position = 1)
    private String resultCode;

    @JsonProperty("error_message")
    @ApiModelProperty(value = "错误信息", position = 2)
    private String errorMessage;

    @JsonProperty("total_records")
    @ApiModelProperty(value = "总记录数", position = 3)
    private Long totalRecords;

    @JsonProperty("total_pages")
    @ApiModelProperty(value = "总页数", position = 4)
    private Integer totalPages;

    @JsonProperty("current_page")
    @ApiModelProperty(value = "当前页", position = 5)
    private Integer currentPage;

    @JsonProperty("contents")
    @ApiModelProperty(value = "内容", position = 6)
    private T contents;

}
