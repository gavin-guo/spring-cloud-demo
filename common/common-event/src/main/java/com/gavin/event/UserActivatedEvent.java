package com.gavin.event;

import lombok.Data;

@Data
public class UserActivatedEvent {

    private String originId;

    private String userId;

}
