package com.gavin.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.exception.PointNotEnoughException;
import com.gavin.model.Response;
import com.gavin.model.dto.point.FreezePointsDto;
import com.gavin.service.PointService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@Slf4j
@Api(value = "/points", description = "积分相关API")
public class PointController {

    private final PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @RequestMapping(value = "/points/usable", method = RequestMethod.GET)
//    @PreAuthorize("#oauth2.hasScope('server') or hasAuthority('AUTH_USER_READ')")
    @ApiOperation(value = "查询指定账户内当前可用积分数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<BigDecimal> queryUsableAmount(
            @ApiParam(name = "account_id", value = "要查询的账户的ID", required = true) @RequestParam(value = "account_id") String _accountId) {
        Response<BigDecimal> response = new Response<>(ResponseCodeConstants.SUCCESS);

        BigDecimal usableAmount = pointService.calculateUsableAmount(_accountId);
        log.info("账户{}内目前可用积分总数为{}。", _accountId, usableAmount);

        response.setContents(usableAmount);
        return response;
    }

    @RequestMapping(value = "/points/freeze", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "为某订单冻结指定数量的积分")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response freezePoints(
            @ApiParam(name = "reservation", value = "冻结积分的相关信息", required = true) @Valid @RequestBody FreezePointsDto _freeze) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        try {
            pointService.freezePoints(_freeze);
            log.info("账户{}冻结积分{}成功，订单：{}。", _freeze.getUserId(), _freeze.getAmount(), _freeze.getOrderId());
        } catch (PointNotEnoughException e) {
            log.warn(e.getMessage());
            response.setCode(ResponseCodeConstants.POINTS_NOT_ENOUGH);
            response.setDetails(e.getMessage());
        }

        return response;
    }

    @RequestMapping(value = "/points/unfreeze", method = RequestMethod.PUT)
    @ApiOperation(value = "解冻为指定订单冻结的所有积分")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response unfreezePoints(
            @ApiParam(name = "order_id", value = "关联的订单ID", required = true) @RequestParam(value = "order_id") String _orderId) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        pointService.unfreezePoints(_orderId);
        log.info("由于订单{}而冻结的积分解冻成功。", _orderId);
        return response;
    }

    @RequestMapping(value = "/points/consume", method = RequestMethod.PUT)
    @ApiOperation(value = "订单完成后从账户最终扣除消费的积分")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response consumePoints(
            @ApiParam(name = "order_id", value = "关联的订单ID", required = true) @RequestParam(value = "order_id") String _orderId) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        pointService.consumePoints(_orderId);
        log.info("订单{}中使用的积分{}从账户最终扣除成功。", _orderId);
        return response;
    }

}
