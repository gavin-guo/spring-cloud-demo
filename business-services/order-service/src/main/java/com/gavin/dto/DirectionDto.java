package com.gavin.dto;

import lombok.Data;

@Data
public class DirectionDto {

    /**
     * 收件人姓名
     */
    private String consignee;

    /**
     * 收件人详细地址
     */
    private String address;

    /**
     * 收件人联系电话
     */
    private String phoneNumber;

}
