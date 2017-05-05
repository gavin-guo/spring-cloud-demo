package com.gavin.controller;

import com.gavin.model.dto.point.FreezePointsDto;
import com.gavin.service.PointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@Slf4j
@Api(value = "/points", description = "积分相关API")
public class PointController {

    @Autowired
    private PointService pointService;

    @RequestMapping(value = "/points/calculation", method = RequestMethod.GET)
//    @PreAuthorize("#oauth2.hasScope('server') or hasAuthority('AUTH_USER_READ')")
    @ApiOperation(value = "查询指定账户内当前可用积分数")
    public BigDecimal queryUsableAmount(
            @ApiParam(name = "account_id", value = "要查询的账户的ID", required = true) @RequestParam(value = "user_id") String _userId) {
        return pointService.calculateUsableAmount(_userId);
    }

    @RequestMapping(value = "/points/freeze", method = RequestMethod.PUT)
    @ApiOperation(value = "为某订单冻结指定数量的积分")
    public void freezePoints(
            @ApiParam(name = "reservation", value = "冻结积分的相关信息", required = true) @Valid @RequestBody FreezePointsDto _freeze) {
        pointService.freezePoints(_freeze);
    }

    @RequestMapping(value = "/points/unfreeze", method = RequestMethod.PUT)
    @ApiOperation(value = "解冻为指定订单冻结的所有积分")
    public void unfreezePoints(
            @ApiParam(name = "order_id", value = "关联的订单ID", required = true) @RequestParam(value = "order_id") String _orderId) {
        pointService.unfreezePoints(_orderId);
    }

    @RequestMapping(value = "/points/consumption", method = RequestMethod.PUT)
    @ApiOperation(value = "订单完成后从账户最终扣除消费的积分")
    public void consumePoints(
            @ApiParam(name = "order_id", value = "关联的订单ID", required = true) @RequestParam(value = "order_id") String _orderId) {
        pointService.consumePoints(_orderId);
    }

}
