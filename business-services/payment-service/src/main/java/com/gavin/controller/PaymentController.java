package com.gavin.controller;

import com.gavin.model.PageResult;
import com.gavin.model.dto.payment.PaymentDto;
import com.gavin.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Api(value = "/payments", description = "支付相关API")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/payments/user/{user_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "分页查询指定用户帐号中所有支付记录")
    public PageResult<PaymentDto> findPaymentsByUserId(
            @ApiParam(name = "user_id", value = "用户ID", required = true) @PathVariable("user_id") String _userId,
            @ApiParam(name = "current_page", value = "当前页", required = true) @RequestParam("current_page") Integer _currentPage,
            @ApiParam(name = "page_size", value = "每页显示记录数", required = true) @RequestParam("page_size") Integer _pageSize) {
        PageRequest pageRequest = new PageRequest(
                _currentPage,
                _pageSize,
                new Sort(Sort.Direction.ASC, "id"));

        return paymentService.findPaymentsByUserId(_userId, pageRequest);
    }

}
