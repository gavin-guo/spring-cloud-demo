package com.gavin.model.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class ItemDto implements Serializable {

    @JsonProperty("product_id")
    @NotNull(message = "product_id不能为空。")
    private String productId;

    @JsonProperty("quantity")
    @NotNull(message = "quantity不能为空")
    @Min(value = 1, message = "quantity不能小于1。")
    private Integer quantity;

}
