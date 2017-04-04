package com.gavin.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class WaitingForPaymentPayload implements Serializable {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("phone")
    private String userId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("amount")
    private BigDecimal amount;

}
