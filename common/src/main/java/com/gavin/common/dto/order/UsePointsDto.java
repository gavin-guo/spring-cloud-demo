package com.gavin.common.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UsePointsDto implements Serializable {

    @JsonProperty("amount")
    @NotNull(message = "'amount' should not be null")
    private BigDecimal amount;

}
