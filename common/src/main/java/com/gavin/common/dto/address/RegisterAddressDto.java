package com.gavin.common.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "RegisterAddressDto", description = "地址登记信息")
public class RegisterAddressDto implements Serializable {

    @JsonProperty("user_id")
    @NotNull(message = "account_id不能为空")
    @ApiModelProperty(value = "关联的账户ID", position = 1)
    private String userId;

    @JsonProperty("consignee")
    @NotNull(message = "consignee不能为空")
    @ApiModelProperty(value = "收件人姓名", position = 2)
    private String consignee;

    @JsonProperty("phone_number")
    @NotNull(message = "phoneNumber不能为空")
    @ApiModelProperty(value = "联系电话", position = 3)
    private String phoneNumber;

    @JsonProperty("zip_code")
    @NotNull(message = "zip_code不能为空")
    @ApiModelProperty(value = "邮政编码", position = 4)
    private String zipCode;

    @JsonProperty("district_id")
    @NotNull(message = "district_id不能为空")
    @ApiModelProperty(value = "行政区", position = 5)
    private String districtId;

    @JsonProperty("street")
    @NotNull(message = "street不能为空")
    @ApiModelProperty(value = "街区", position = 6)
    private String street;

    @JsonProperty("building")
    @NotNull(message = "building不能为空")
    @ApiModelProperty(value = "大楼", position = 7)
    private String building;

    @JsonProperty("room")
    @NotNull(message = "room不能为空")
    @ApiModelProperty(value = "房间号", position = 8)
    private String room;

    @JsonProperty("default_flag")
    @NotNull(message = "default_flag不能为空")
    @ApiModelProperty(value = "是否默认地址", position = 9)
    private Boolean defaultFlag;

    @JsonProperty("comment")
    @ApiModelProperty(value = "备注", position = 10)
    private String comment;

}
