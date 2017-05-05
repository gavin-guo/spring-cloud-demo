package com.gavin.service;

import com.gavin.model.PageResult;
import com.gavin.model.dto.payment.NotifyPaidDto;
import com.gavin.model.dto.payment.PaymentDto;
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
    void calledByThirdParty(NotifyPaidDto _notification);

}
