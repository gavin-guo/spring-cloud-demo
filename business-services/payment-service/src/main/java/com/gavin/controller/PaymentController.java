package com.gavin.controller;

import com.gavin.constants.ResponseCodeConstants;
import com.gavin.model.PageArgument;
import com.gavin.model.Response;
import com.gavin.model.dto.payment.NotifyPaidDto;
import com.gavin.model.vo.payment.PaymentVo;
import com.gavin.service.PaymentService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Api(value = "/payments", description = "支付相关API")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "/payments/user/{user_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "分页查询指定用户帐号中所有支付记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response<List<PaymentVo>> findPaymentsByUserId(
            @ApiParam(name = "user_id", value = "用户ID", required = true) @PathVariable("user_id") String _userId,
            @ApiParam(name = "current_page", value = "当前页", required = true) @RequestParam("current_page") Integer _currentPage,
            @ApiParam(name = "page_size", value = "每页显示记录数", required = true) @RequestParam("page_size") Integer _pageSize) {
        Response<List<PaymentVo>> response = new Response<>(ResponseCodeConstants.SUCCESS);

        PageArgument pageArgument = new PageArgument();
        pageArgument.setCurrentPage(_currentPage);
        pageArgument.setPageSize(_pageSize);

        List<PaymentVo> paymentVos = paymentService.findPaymentsByUserId(_userId, pageArgument);
        response.setContents(paymentVos);
        response.setPage(pageArgument);
        return response;
    }

    @RequestMapping(value = "/payments/notify", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "供第三方支付平台调用，接收支付结果通知")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", dataType = "String", required = true, value = "Token", defaultValue = "bearer ")
    })
    public Response notifiedByThirdParty(
            @ApiParam(name = "notification", value = "通知信息", required = true) @Valid @RequestBody NotifyPaidDto _notification) {
        Response response = new Response(ResponseCodeConstants.SUCCESS);

        paymentService.notifiedByThirdParty(_notification);
        return response;
    }

}
