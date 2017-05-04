package com.gavin.service;

import com.gavin.dto.DirectionDto;
import com.gavin.enums.OrderStatusEnums;
import com.gavin.event.ArrangeShipmentEvent;
import com.gavin.event.CancelReservationEvent;
import com.gavin.event.WaitingForPaymentEvent;
import com.gavin.model.PageArgument;
import com.gavin.model.dto.order.CreateOrderDto;
import com.gavin.model.dto.order.OrderDetailsDto;
import com.gavin.model.vo.order.OrderDetailsVo;
import com.gavin.model.vo.order.OrderVo;

import java.math.BigDecimal;
import java.util.List;

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
    OrderDetailsVo findOrderById(String _orderId);

    /**
     * 根据账户ID查找关联订单。
     *
     * @param _userId       用户ID
     * @param _pageArgument 分页设置
     * @return
     */
    List<OrderVo> findOrdersByUserId(String _userId, PageArgument _pageArgument);

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

    /**
     * 发布取消保留商品的事件。
     *
     * @param _event
     * @return
     */
    boolean publishCancelReservationEvent(CancelReservationEvent _event);

    /**
     * 发布安排发货的事件。
     *
     * @param _event
     * @return
     */
    boolean publishArrangeShipmentEvent(ArrangeShipmentEvent _event);

    /**
     * 发布生成支付请求的事件。
     *
     * @param _event
     * @return
     */
    boolean publishWaitingForPaymentEvent(WaitingForPaymentEvent _event);

}
