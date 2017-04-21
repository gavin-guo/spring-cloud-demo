package com.gavin.event;

import lombok.Data;

@Data
public class UserCreatedEvent {

    private String originId;

    private String userId;

    private String loginName;

    private String nickName;

    private String email;

    private String phone;

}
