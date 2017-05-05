package com.gavin.service;

import com.gavin.dto.DirectionDto;
import com.gavin.dto.PageResult;
import com.gavin.dto.dto.order.CreateOrderDto;
import com.gavin.dto.dto.order.OrderDetailsDto;
import com.gavin.dto.dto.order.OrderDto;
import com.gavin.enums.OrderStatusEnums;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

public interface OrderService {

    /**
     * 创建订单。
     *
     * @param _order
     * @return
     */
    OrderDetailsDto createOrder(CreateOrderDto _order);

    /**
     * 更新订单状态。
     *
     * @param _orderId 订单ID
     * @param _status  状态
     */
    void updateOrderStatus(String _orderId, OrderStatusEnums _status);

    /**
     * 根据订单ID查找订单。
     *
     * @param _orderId 订单ID
     * @return
     */
    OrderDetailsDto findOrderById(String _orderId);

    /**
     * 根据账户ID查找关联订单。
     *
     * @param _userId      用户ID
     * @param _pageRequest 分页设置
     * @return
     */
    PageResult<OrderDto> findOrdersByUserId(String _userId, PageRequest _pageRequest);

    /**
     * 根据订单ID查询收件人姓名和地址。
     *
     * @param _orderId 订单ID
     * @return
     */
    DirectionDto findDirectionByOrderId(String _orderId);

    /**
     * 取消订单。
     *
     * @param _orderId 订单ID
     */
    void cancelOrder(String _orderId);

    /**
     * 支付订单。
     *
     * @param _orderId      订单ID
     * @param _pointsAmount 使用积分数
     */
    void payOrder(String _orderId, BigDecimal _pointsAmount);

    /**
     * 订单支付成功。
     *
     * @param _orderId 订单ID
     */
    void succeedInPayment(String _orderId);

}
