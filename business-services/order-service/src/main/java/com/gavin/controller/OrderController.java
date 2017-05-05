package com.gavin.controller;

import com.gavin.constants.RequestAttributeConstants;
import com.gavin.model.PageResult;
import com.gavin.model.dto.order.CreateOrderDto;
import com.gavin.model.dto.order.OrderDetailsDto;
import com.gavin.model.dto.order.OrderDto;
import com.gavin.model.dto.security.CurrentUser;
import com.gavin.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@Slf4j
@Api(value = "/orders", description = "订单相关API")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/orders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "创建订单")
    public OrderDetailsDto createOrder(
            @ApiParam(name = "order", value = "订单信息", required = true) @Valid @RequestBody CreateOrderDto _order,
            @RequestAttribute(name = RequestAttributeConstants.CURRENT_USER, required = false) CurrentUser _currentUser) {
        return orderService.createOrder(_order);
    }

    @RequestMapping(value = "/orders/{order_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询单个订单信息")
    public OrderDetailsDto findOrderById(
            @ApiParam(name = "order_id", value = "订单ID", required = true) @PathVariable("order_id") String _orderId) {
        return orderService.findOrderById(_orderId);
    }

    @RequestMapping(value = "/orders/user/{user_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "分页查询指定用户帐号下所有订单信息")
    public PageResult<OrderDto> findOrdersByAccountId(
            @ApiParam(name = "user_id", value = "用户ID", required = true) @PathVariable("user_id") String _userId,
            @ApiParam(name = "current_page", value = "当前页", required = true) @RequestParam("current_page") Integer _currentPage,
            @ApiParam(name = "page_size", value = "每页显示记录数", required = true) @RequestParam("page_size") Integer _pageSize) {
        PageRequest pageRequest = new PageRequest(
                _currentPage,
                _pageSize,
                new Sort(Sort.Direction.ASC, "id"));

        return orderService.findOrdersByUserId(_userId, pageRequest);
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
