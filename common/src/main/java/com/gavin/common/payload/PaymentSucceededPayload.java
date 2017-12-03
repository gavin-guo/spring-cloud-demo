package com.gavin.common.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentSucceededPayload implements Serializable {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("order_id")
    private String orderId;

}
