package com.gavin.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.PageArgument;
import com.gavin.model.Response;
import com.gavin.model.dto.order.CreateOrderDto;
import com.gavin.model.vo.order.OrderDetailsVo;
import com.gavin.model.vo.order.OrderVo;
import com.gavin.service.OrderService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/orders", description = "订单相关API")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/orders", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "创建订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<OrderDetailsVo> createOrder(
            @ApiParam(name = "order", value = "订单信息", required = true) @Valid @RequestBody CreateOrderDto _order) {
        Response<OrderDetailsVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        OrderDetailsVo orderDetailsVo = orderService.createOrder(_order);
        log.info("订单{}创建成功。", orderDetailsVo.getId());

        response.setContents(orderDetailsVo);
        return response;
    }

    @RequestMapping(value = "/orders/{order_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询单个订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<OrderDetailsVo> findOrderById(
            @ApiParam(name = "order_id", value = "订单ID", required = true) @PathVariable("order_id") String _orderId) {
        Response<OrderDetailsVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        OrderDetailsVo orderDetailsVo = orderService.findOrderById(_orderId);
        response.setContents(orderDetailsVo);
        return response;
    }

    @RequestMapping(value = "/orders/user/{user_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "分页查询指定用户帐号下所有订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<List<OrderVo>> findOrdersByAccountId(
            @ApiParam(name = "user_id", value = "用户ID", required = true) @PathVariable("user_id") String _userId,
            @ApiParam(name = "current_page", value = "当前页", required = true) @RequestParam("current_page") Integer _currentPage,
            @ApiParam(name = "page_size", value = "每页显示记录数", required = true) @RequestParam("page_size") Integer _pageSize) {
        Response<List<OrderVo>> response = new Response<>(ResponseCodeConstants.SUCCESS);

        PageArgument pageArgument = new PageArgument();
        pageArgument.setCurrentPage(_currentPage);
        pageArgument.setPageSize(_pageSize);

        List<OrderVo> orderVos = orderService.findOrdersByUserId(_userId, pageArgument);
        response.setContents(orderVos);
        response.setPage(pageArgument);
        return response;
    }

    @RequestMapping(value = "/orders/{order_id}/cancellation", method = RequestMethod.PUT)
    @ApiOperation(value = "取消订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response cancelOrder(
            @ApiParam(name = "order_id", value = "要取消的订单ID", required = true) @PathVariable("order_id") String _orderId) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        orderService.cancelOrder(_orderId);
        return response;
    }

    @RequestMapping(value = "/orders/{order_id}/payment", method = RequestMethod.PUT)
    @ApiOperation(value = "支付订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response payOrder(
            @ApiParam(name = "order_id", value = "要支付的订单ID", required = true) @PathVariable("order_id") String _orderId,
            @ApiParam(name = "points_amount", value = "使用积分数量") @RequestParam(name = "points_amount", required = false) BigDecimal _pointsAmount) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        orderService.payOrder(_orderId, _pointsAmount);
        return response;
    }

}
