package com.gavin.common.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "RegisterAddressDto", description = "地址登记信息")
public class RegisterAddressDto implements Serializable {

    @JsonProperty("consignee")
    @NotNull(message = "'consignee' should not be null")
    @ApiModelProperty(value = "收件人姓名", position = 1)
    private String consignee;

    @JsonProperty("phone_number")
    @NotNull(message = "'phone_number' should not be null")
    @ApiModelProperty(value = "联系电话", position = 2)
    private String phoneNumber;

    @JsonProperty("zip_code")
    @NotNull(message = "'zip_code' should not be null")
    @ApiModelProperty(value = "邮政编码", position = 3)
    private String zipCode;

    @JsonProperty("district_id")
    @NotNull(message = "'district_id' should not be null")
    @ApiModelProperty(value = "行政区", position = 4)
    private String districtId;

    @JsonProperty("street")
    @NotNull(message = "'street' should not be null")
    @ApiModelProperty(value = "街区", position = 5)
    private String street;

    @JsonProperty("building")
    @NotNull(message = "'building' should not be null")
    @ApiModelProperty(value = "大楼", position = 6)
    private String building;

    @JsonProperty("room")
    @NotNull(message = "'room' should not be null")
    @ApiModelProperty(value = "房间号", position = 7)
    private String room;

    @JsonProperty("default_address")
    @NotNull(message = "'default_address' should not be null")
    @ApiModelProperty(value = "是否默认地址", position = 8)
    private Boolean defaultAddress;

    @JsonProperty("comment")
    @ApiModelProperty(value = "备注", position = 9)
    private String comment;

}
