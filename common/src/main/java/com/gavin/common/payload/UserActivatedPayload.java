package com.gavin.common.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserActivatedPayload implements Serializable {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("user_id")
    private String userId;

}
