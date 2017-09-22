package com.gavin.dto.delivery;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DeliveryDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("consignee")
    private String consignee;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("carrier_name")
    private String carrierName;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_time")
    private Date createdTime;

    @JsonProperty("modified_time")
    private Date modifiedTime;

}
