package com.gavin.business.service;

import com.gavin.common.dto.common.PageResult;
import com.gavin.common.dto.order.NotifyPaidDto;
import com.gavin.common.dto.order.PaymentDto;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

public interface PaymentService {

    /**
     * 创建支付记录。
     *
     * @param _userId  用户ID
     * @param _orderId 订单ID
     * @param _amount  支付金额
     */
    String createPayment(String _userId, String _orderId, BigDecimal _amount);

    /**
     * 根据用户ID查找关联支付记录。
     *
     * @param _userId      用户ID
     * @param _pageRequest 分页设置
     * @return
     */
    PageResult<PaymentDto> findPaymentsByUserId(String _userId, PageRequest _pageRequest);

    /**
     * 第三方支付平台通知支付成功后的处理。
     *
     * @param _notification
     */
    void paid(NotifyPaidDto _notification);

}
