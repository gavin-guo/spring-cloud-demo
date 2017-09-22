package com.gavin.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayMoneyDto implements Serializable {

    @JsonProperty("amount")
    @NotNull(message = "amount不能为空")
    private BigDecimal amount;

    @JsonProperty("payment_method")
    @NotNull(message = "payment_method不能为空")
    private Byte paymentMethod;

}
