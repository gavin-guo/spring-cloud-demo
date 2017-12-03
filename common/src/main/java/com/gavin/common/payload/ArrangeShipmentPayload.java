package com.gavin.common.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArrangeShipmentPayload implements Serializable {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("consignee")
    private String consignee;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;

}
