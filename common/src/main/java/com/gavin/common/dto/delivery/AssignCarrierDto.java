package com.gavin.common.dto.delivery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AssignCarrierDto implements Serializable {

    @JsonProperty("carrier_id")
    @NotNull(message = "carrier_id不能为空")
    private String carrierId;

    @JsonProperty("tracking_number")
    @NotNull(message = "tracking_number不能为空")
    private String trackingNumber;

}
