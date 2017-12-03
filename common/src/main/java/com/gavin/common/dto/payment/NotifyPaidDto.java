package com.gavin.common.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class NotifyPaidDto implements Serializable {

    @JsonProperty("payment_id")
    @NotNull(message = "payment_id不能为空")
    private String paymentId;

    @JsonProperty("amount")
    @NotNull(message = "amount不能为空")
    private BigDecimal amount;

}
