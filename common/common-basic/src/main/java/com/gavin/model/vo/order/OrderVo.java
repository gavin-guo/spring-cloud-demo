package com.gavin.model.vo.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "userId",
        "status",
        "totalAmount",
        "rewardPoints",
        "createdTime",
        "modifiedTime"})
@Data
public class OrderVo implements Serializable {

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

    @JsonProperty("created_time")
    private Date createdTime;

    @JsonProperty("modified_time")
    private Date modifiedTime;

}
