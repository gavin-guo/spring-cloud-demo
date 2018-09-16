package com.gavin.common.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AssignCarrierDto implements Serializable {

    @JsonProperty("carrier_id")
    @NotNull(message = "'carrier_id' should not be null")
    private String carrierId;

    @JsonProperty("tracking_number")
    @NotNull(message = "'tracking_number' should not be null")
    private String trackingNumber;

}
