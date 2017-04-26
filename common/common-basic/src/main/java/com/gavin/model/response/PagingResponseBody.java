package com.gavin.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonPropertyOrder({
        "totalRecords",
        "totalPages",
        "currentPage",
        "contents"})
@Data
@NoArgsConstructor
@ApiModel(value = "PagingResponse", description = "分页信息")
public class PagingResponseBody<T> {

    @JsonProperty("total_records")
    @ApiModelProperty(value = "总记录数", position = 1)
    private Long totalRecords;

    @JsonProperty("total_pages")
    @ApiModelProperty(value = "总页数", position = 2)
    private Integer totalPages;

    @JsonProperty("current_page")
    @ApiModelProperty(value = "当前页", position = 3)
    private Integer currentPage;

    @JsonProperty("contents")
    @ApiModelProperty(value = "内容", position = 4)
    private T contents;

}
