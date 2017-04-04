package com.gavin.event;

import lombok.Data;

@Data
public class PaymentSucceededEvent {

    private String originId;

    private String orderId;

}
