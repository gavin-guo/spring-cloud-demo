package com.gavin.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.enums.DeliveryStatusEnums;
import com.gavin.model.Response;
import com.gavin.model.dto.delivery.AssignCarrierDto;
import com.gavin.model.vo.delivery.DeliveryVo;
import com.gavin.service.DeliveryService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@Api(value = "/deliveries", description = "物流相关API")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    @RequestMapping(value = "/deliveries/{delivery_id}/assignment", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "分配物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<DeliveryVo> assignCarrier(
            @ApiParam(name = "delivery_id", value = "物流记录ID", required = true) @PathVariable("delivery_id") String _deliveryId,
            @ApiParam(name = "assignment", value = "物流公司信息", required = true) @Valid @RequestBody AssignCarrierDto _assignment) {
        Response<DeliveryVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        DeliveryVo deliveryVo = deliveryService.assignCarrier(_deliveryId, _assignment);
        response.setContents(deliveryVo);
        return response;
    }

    @RequestMapping(value = "/deliveries/order/{order_id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "根据订单ID查询物流信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<DeliveryVo> findDeliveryByOrderId(
            @ApiParam(name = "order_id", value = "订单ID", required = true) @PathVariable("order_id") String _orderId) {
        Response<DeliveryVo> response = new Response<>(ResponseCodeConstants.SUCCESS);

        DeliveryVo deliveryVo = deliveryService.findDeliveryByOrderId(_orderId);
        response.setContents(deliveryVo);
        return response;
    }

    @RequestMapping(value = "/deliveries/{delivery_id}/shipped", method = RequestMethod.PUT)
    @ApiOperation(value = "更新物流状态为已发货")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response notifiedForShipped(
            @ApiParam(name = "delivery_id", value = "物流记录ID", required = true) @PathVariable("delivery_id") String _deliveryId) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        deliveryService.updateDeliveryStatus(_deliveryId, DeliveryStatusEnums.SHIPPED.name());
        return response;
    }

    @RequestMapping(value = "/deliveries/{delivery_id}/delivered", method = RequestMethod.PUT)
    @ApiOperation(value = "更新物流状态为已送达")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response notifiedForDelivered(
            @ApiParam(name = "delivery_id", value = "物流记录ID", required = true) @PathVariable("delivery_id") String _deliveryId) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        deliveryService.updateDeliveryStatus(_deliveryId, DeliveryStatusEnums.DELIVERED.name());
        return response;
    }

}
