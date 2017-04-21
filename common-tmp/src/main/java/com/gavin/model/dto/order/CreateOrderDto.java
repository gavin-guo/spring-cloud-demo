package com.gavin.model.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "CreateOrderDto", description = "订单创建信息")
public class CreateOrderDto implements Serializable {

    @JsonProperty("user_id")
    @NotNull(message = "user_id不能为空。")
    @ApiModelProperty(value = "账户ID", position = 1, required = true)
    private String userId;

    @JsonProperty("address_id")
    @NotNull(message = "address_id不能为空。")
    @ApiModelProperty(value = "送货地址ID", position = 2, required = true)
    private String addressId;

    @JsonProperty("items")
    @NotNull
    @Valid
    @ApiModelProperty(value = "商品信息", position = 3, required = true)
    private List<ItemDto> items;

}
