package com.gavin.controller;

import com.gavin.constants.RequestHeaderConstants;
import com.gavin.model.PageResult;
import com.gavin.model.dto.order.CreateOrderDto;
import com.gavin.model.dto.order.OrderDetailsDto;
import com.gavin.model.dto.order.OrderDto;
import com.gavin.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@Slf4j
@Api(value = "/orders", description = "订单相关API")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @ApiOperation(value = "创建订单")
    public OrderDetailsDto createOrder(
            @ApiParam(name = "order", value = "订单信息", required = true) @Valid @RequestBody CreateOrderDto _order,
            @RequestHeader(RequestHeaderConstants.CURRENT_USER_ID) String _userId) {
        return orderService.createOrder(_userId, _order);
    }

    @RequestMapping(value = "/orders/{order_id}", method = RequestMethod.GET)
    @ApiOperation(value = "查询单个订单信息")
    public OrderDetailsDto findOrderById(
            @ApiParam(name = "order_id", value = "订单ID", required = true) @PathVariable("order_id") String _orderId) {
        return orderService.findOrderById(_orderId);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    @ApiOperation(value = "分页查询当前用户的所有订单信息")
    public PageResult<OrderDto> findOrders(
            @ApiParam(name = "x-user-id", value = "用户ID", required = true) @RequestHeader(RequestHeaderConstants.CURRENT_USER_ID) String _userId,
            @ApiParam(name = "current_page", value = "当前页") @RequestParam(name = "current_page", defaultValue = "1") Integer _currentPage,
            @ApiParam(name = "page_size", value = "每页显示记录数") @RequestParam(name = "page_size", defaultValue = "10") Integer _pageSize) {
        PageRequest pageRequest = new PageRequest(
                _currentPage - 1,
                _pageSize,
                new Sort(Sort.Direction.ASC, "id"));

        PageResult<OrderDto> pageResult = orderService.findOrdersByUserId(_userId, pageRequest);
        pageResult.setCurrentPage(_currentPage);
        return pageResult;
    }

    @RequestMapping(value = "/orders/cancellation", method = RequestMethod.PUT)
    @ApiOperation(value = "取消订单")
    public void cancelOrder(
            @ApiParam(name = "order_id", value = "要取消的订单ID", required = true) @RequestParam("order_id") String _orderId) {
        orderService.cancelOrder(_orderId);
    }

    @RequestMapping(value = "/orders/payment", method = RequestMethod.PUT)
    @ApiOperation(value = "支付订单")
    public void payOrder(
            @ApiParam(name = "order_id", value = "要支付的订单ID", required = true) @RequestParam("order_id") String _orderId,
            @ApiParam(name = "points_amount", value = "使用积分数量") @RequestParam(name = "points_amount", required = false) BigDecimal _pointsAmount) {
        orderService.payOrder(_orderId, _pointsAmount);
    }

}
