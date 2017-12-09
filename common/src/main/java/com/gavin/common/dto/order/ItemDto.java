package com.gavin.common.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "productId",
        "quantity"})
@Data
public class ItemDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("product_id")
    @NotNull(message = "'product_id' should not be null")
    private String productId;

    @JsonProperty("quantity")
    @NotNull(message = "'quantity' should not be null")
    @Min(value = 1, message = "'quantity' should larger than 1")
    private Integer quantity;

}
