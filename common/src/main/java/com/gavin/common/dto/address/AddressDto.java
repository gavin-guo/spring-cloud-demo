package com.gavin.common.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "userId",
        "consignee",
        "phoneNumber",
        "zipCode",
        "address",
        "defaultFlag",
        "comment"})
@Data
@ApiModel(value = "AddressDto", description = "地址信息")
public class AddressDto implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("consignee")
    private String consignee;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("zip_code")
    private String zipCode;

    @JsonProperty("address")
    private String address;

    @JsonProperty("default_flag")
    private Boolean defaultFlag;

    @JsonProperty("comment")
    private String comment;

}
