package com.gavin.common.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "userId",
        "amount",
        "expireDate",
        "createdTime",
        "modifiedTime"})
@Data
@ApiModel(value = "Point", description = "积分信息")
public class PointDto implements Serializable {

    @JsonProperty("id")
    @ApiModelProperty(value = "积分ID", position = 1)
    private String id;

    @JsonProperty("user_id")
    @ApiModelProperty(value = "积分所属的用户ID", position = 2)
    private String userId;

    @JsonProperty("amount")
    @ApiModelProperty(value = "积分数量", position = 3)
    private BigDecimal amount;

    @JsonProperty("expire_date")
    @ApiModelProperty(value = "积分过期日期", position = 4)
    private String expireDate;

    @JsonProperty("created_time")
    @ApiModelProperty(value = "创建时间", position = 5)
    private Date createdTime;

    @JsonProperty("modified_time")
    @ApiModelProperty(value = "修改时间", position = 6)
    private Date modifiedTime;

}
