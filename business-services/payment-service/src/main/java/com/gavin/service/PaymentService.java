package com.gavin.service;

import com.gavin.event.PaymentSucceededEvent;
import com.gavin.model.PageArgument;
import com.gavin.model.dto.payment.NotifyPaidDto;
import com.gavin.model.vo.payment.PaymentVo;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {

    /**
     * 创建支付记录。
     *
     * @param _userId  用户ID
     * @param _orderId 订单ID
     * @param _amount  支付金额
     */
    void createPayment(String _userId, String _orderId, BigDecimal _amount);

    /**
     * 根据用户ID查找关联支付记录。
     *
     * @param _userId       用户ID
     * @param _pageArgument 分页设置
     * @return
     */
    List<PaymentVo> findPaymentsByUserId(String _userId, PageArgument _pageArgument);

    /**
     * 第三方支付平台通知支付成功后的处理。
     *
     * @param _notification
     */
    void notifiedByThirdParty(NotifyPaidDto _notification);

    /**
     * 更新支付状态
     *
     * @param _paymentId
     * @param _status
     */
    void updatePaymentStatus(String _paymentId, String _status);

    /**
     * 发布支付已成功的事件
     *
     * @param _event
     * @return
     */
    boolean publishPaymentSucceededEvent(PaymentSucceededEvent _event);

}
