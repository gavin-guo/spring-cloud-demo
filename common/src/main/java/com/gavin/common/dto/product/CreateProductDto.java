package com.gavin.common.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CreateProductDto implements Serializable {

    @JsonProperty("name")
    @NotNull(message = "name不能为空")
    private String name;

    @JsonProperty("category_id")
    @NotNull(message = "category_id不能为空")
    private String categoryId;

    @JsonProperty("price")
    @NotNull(message = "price不能为空")
    private Float price;

    @JsonProperty("stocks")
    @NotNull(message = "stock不能为空")
    private Integer stocks;

    @JsonProperty("comment")
    @NotNull(message = "comment不能为空")
    private String comment;

}
