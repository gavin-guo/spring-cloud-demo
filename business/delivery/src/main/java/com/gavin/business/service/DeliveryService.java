package com.gavin.business.service;

import com.gavin.common.dto.delivery.AssignCarrierDto;
import com.gavin.common.dto.delivery.DeliveryDto;

public interface DeliveryService {

    /**
     * 创建物流记录。
     *
     * @param _orderId     订单ID
     * @param _consignee   收件人姓名
     * @param _address     收件人地址
     * @param _phoneNumber 收件人联系电话
     * @return
     */
    void createDelivery(String _orderId, String _consignee, String _address, String _phoneNumber);

    /**
     * 指派快递公司。
     *
     * @param _deliveryId 物流ID
     * @param _assignment 快递公司信息
     * @return
     */
    DeliveryDto assignCarrier(String _deliveryId, AssignCarrierDto _assignment);

    /**
     * 根据订单ID查询物流信息。
     *
     * @param _orderId 订单ID
     * @return
     */
    DeliveryDto findDeliveryByOrderId(String _orderId);

    /**
     * 更新物流状态。
     *
     * @param _deliveryId 物流ID
     * @param _status     状态
     */
    void updateDeliveryStatus(String _deliveryId, String _status);

}
