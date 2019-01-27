package com.gavin.business.service.impl;

import com.gavin.business.domain.Payment;
import com.gavin.business.repository.PaymentRepository;
import com.gavin.business.service.PaymentService;
import com.gavin.common.dto.common.PageResult;
import com.gavin.common.dto.order.NotifyPaidDto;
import com.gavin.common.dto.order.PaymentDto;
import com.gavin.common.enums.PaymentStatusEnums;
import com.gavin.common.exception.RecordNotFoundException;
import com.gavin.common.messaging.PaymentSucceededProcessor;
import com.gavin.common.payload.PaymentSucceededPayload;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentSucceededProcessor paymentSucceededProcessor;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public String createPayment(String _userId, String _orderId, BigDecimal _amount) {
        Payment payment = new Payment();
        payment.setUserId(_userId);
        payment.setOrderId(_orderId);
        payment.setAmount(_amount);
        payment.setStatus(PaymentStatusEnums.CREATED);
        paymentRepository.save(payment);
        return payment.getId();
    }

    @Override
    @Transactional
    public void paid(NotifyPaidDto _notification) {
        String paymentId = _notification.getPaymentId();
        BigDecimal amount = _notification.getAmount();

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RecordNotFoundException("payment", paymentId));

        // 第三方支付平台反馈的支付成功金额与需要支付金额不一致。
        if (amount.compareTo(payment.getAmount()) != 0) {
            log.warn("paid amount are not equal to expected.");
            return;
        }

        payment.setStatus(PaymentStatusEnums.PAID);
        paymentRepository.save(payment);

        // 发送消息至order。
        PaymentSucceededPayload payload = new PaymentSucceededPayload();
        payload.setOrderId(payment.getOrderId());

        Message<PaymentSucceededPayload> message = MessageBuilder.withPayload(payload).build();
        paymentSucceededProcessor.producer().send(message);
    }

    @Override
    public PageResult<PaymentDto> findPaymentsByUserId(String _userId, PageRequest _pageRequest) {
        Page<Payment> payments = paymentRepository.findByUserId(_userId, _pageRequest);

        List<PaymentDto> paymentDtos = new ArrayList<>();
        payments.forEach(
                payment -> {
                    PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
                    paymentDtos.add(paymentDto);
                }
        );

        PageResult<PaymentDto> pageResult = new PageResult<>();
        pageResult.setTotalRecords(payments.getTotalElements());
        pageResult.setTotalPages(payments.getTotalPages());
        pageResult.setRecords(paymentDtos);

        return pageResult;
    }

}
