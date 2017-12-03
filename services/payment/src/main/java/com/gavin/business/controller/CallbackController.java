package com.gavin.business.controller;

import com.gavin.common.dto.payment.NotifyPaidDto;
import com.gavin.business.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class CallbackController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/payments/callback", method = RequestMethod.PUT)
    @ApiOperation(value = "供第三方支付平台调用，接收支付结果通知")
    public void callback(
            @ApiParam(name = "notification", value = "通知信息", required = true) @Valid @RequestBody NotifyPaidDto _notification) {
        paymentService.calledByThirdParty(_notification);
    }

}
