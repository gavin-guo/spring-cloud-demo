package com.gavin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonPropertyOrder({
        "currentPage",
        "pageSize",
        "totalPages",
        "totalElements"})
@Data
@ApiModel(value = "Page", description = "分页信息")
public class PageArgument {

    @JsonProperty("current_page")
    @ApiModelProperty(value = "当前页", position = 1)
    private Integer currentPage;

    @JsonProperty("page_size")
    @ApiModelProperty(value = "每页显示记录数", position = 2)
    private Integer pageSize;

    @JsonProperty("total_pages")
    @ApiModelProperty(value = "总页数", position = 3)
    private Integer totalPages;

    @JsonProperty("total_elements")
    @ApiModelProperty(value = "总记录数", position = 4)
    private Long totalElements;

    public PageArgument() {
    }

    public PageArgument(Integer currentPage, Integer pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

}
