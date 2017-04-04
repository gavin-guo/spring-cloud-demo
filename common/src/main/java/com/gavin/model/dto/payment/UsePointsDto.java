package com.gavin.model.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UsePointsDto implements Serializable {

    @JsonProperty("amount")
    @NotNull(message = "amount不能为空")
    private BigDecimal amount;

}
