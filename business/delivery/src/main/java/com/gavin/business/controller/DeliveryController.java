package com.gavin.business.controller;

import com.gavin.business.service.DeliveryService;
import com.gavin.common.dto.delivery.AssignCarrierDto;
import com.gavin.common.dto.delivery.DeliveryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @RequestMapping(value = "/deliveries/assignment", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "分配物流公司")
    public DeliveryDto assignCarrier(
            @ApiParam(name = "delivery_id", value = "物流记录ID", required = true) @RequestParam("delivery_id") String _deliveryId,
            @ApiParam(name = "assignment", value = "物流公司信息", required = true) @Valid @RequestBody AssignCarrierDto _assignment) {
        return deliveryService.assignCarrier(_deliveryId, _assignment);
    }

    @RequestMapping(value = "/deliveries/order/{order_id}", method = RequestMethod.GET)
    @ApiOperation(value = "根据订单ID查询物流信息")
    public DeliveryDto findDeliveryByOrderId(
            @ApiParam(name = "order_id", value = "订单ID", required = true) @PathVariable("order_id") String _orderId) {
        return deliveryService.findDeliveryByOrderId(_orderId);
    }

}
