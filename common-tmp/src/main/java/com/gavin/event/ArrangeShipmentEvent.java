package com.gavin.event;

import lombok.Data;

@Data
public class ArrangeShipmentEvent {

    private String originId;

    private String orderId;

    private String consignee;

    private String address;

    private String phoneNumber;

}
