package com.gavin.event;

import lombok.Data;

@Data
public class CancelReservationEvent {

    private String originId;

    private String orderId;

}
