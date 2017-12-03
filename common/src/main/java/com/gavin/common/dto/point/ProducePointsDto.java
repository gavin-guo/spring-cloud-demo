package com.gavin.common.dto.point;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "ProducePointsDto", description = "新增积分信息")
public class ProducePointsDto implements Serializable {

    @JsonProperty("user_id")
    @NotNull(message = "user_id不能为空")
    @ApiModelProperty(value = "积分所属的用户ID", position = 1, required = true)
    private String userId;

    @JsonProperty("order_id")
    @NotNull(message = "order_id不能为空")
    @ApiModelProperty(value = "产生积分的订单ID", position = 2, required = true)
    private String orderId;

    @JsonProperty("amount")
    @NotNull(message = "amount不能为空")
    @ApiModelProperty(value = "积分数量", position = 3, required = true)
    private BigDecimal amount;

}
