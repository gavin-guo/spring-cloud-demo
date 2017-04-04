package com.gavin.event;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaitingForPaymentEvent {

    private String originId;

    private String userId;

    private String orderId;

    private BigDecimal amount;

}
