package com.gavin.model.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "categoryId",
        "categoryName",
        "price",
        "stocks",
        "comment",
        "createdTime",
        "modifiedTime"})
@Data
public class ProductDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("category_id")
    private String categoryId;

    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("price")
    private Float price;

    @JsonProperty("stocks")
    private Integer stocks;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("created_time")
    private Date createdTime;

    @JsonProperty("modified_time")
    private Date modifiedTime;

}
