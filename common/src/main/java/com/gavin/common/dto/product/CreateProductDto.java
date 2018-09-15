package com.gavin.common.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CreateProductDto implements Serializable {

    @JsonProperty("name")
    @NotNull(message = "'name' should not be null")
    private String name;

    @JsonProperty("category_id")
    @NotNull(message = "'category_id' should not be null")
    private String categoryId;

    @JsonProperty("price")
    @NotNull(message = "'price' should not be null")
    private BigDecimal price;

    @JsonProperty("stocks")
    @NotNull(message = "'stock' should not be null")
    private Integer stocks;

    @JsonProperty("comment")
    @NotNull(message = "'comment' should not be null")
    private String comment;

}
