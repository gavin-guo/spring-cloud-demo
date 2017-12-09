package com.gavin.common.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PayMoneyDto implements Serializable {

    @JsonProperty("amount")
    @NotNull(message = "'amount' should not be null")
    private BigDecimal amount;

    @JsonProperty("payment_method")
    @NotNull(message = "'payment_method' should not be null")
    private Byte paymentMethod;

}
