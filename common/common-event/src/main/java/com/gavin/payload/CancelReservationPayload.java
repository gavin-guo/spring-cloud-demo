package com.gavin.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CancelReservationPayload implements Serializable {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("order_id")
    private String orderId;

}
