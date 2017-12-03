package com.gavin.common.dto.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "userId",
        "addressId",
        "status",
        "totalAmount",
        "rewardPoints",
        "consignee",
        "address",
        "phoneNumber",
        "items"})
@Data
public class OrderDetailsDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("reward_points")
    private BigDecimal rewardPoints;

    @JsonProperty("consignee")
    private String consignee;

    @JsonProperty("address")
    private String address;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("items")
    private List<ItemDto> items;

}
