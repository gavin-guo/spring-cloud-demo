package com.gavin.service.impl;

import com.gavin.entity.PaymentEntity;
import com.gavin.enums.PaymentStatusEnums;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.messaging.PaymentSucceededProcessor;
import com.gavin.model.PageResult;
import com.gavin.model.dto.payment.NotifyPaidDto;
import com.gavin.model.dto.payment.PaymentDto;
import com.gavin.payload.PaymentSucceededPayload;
import com.gavin.repository.PaymentRepository;
import com.gavin.service.PaymentService;
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
import java.util.Optional;

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
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setUserId(_userId);
        paymentEntity.setOrderId(_orderId);
        paymentEntity.setAmount(_amount);
        paymentEntity.setStatus(PaymentStatusEnums.CREATED);
        paymentRepository.save(paymentEntity);
        return paymentEntity.getId();
    }

    @Override
    @Transactional
    public void calledByThirdParty(NotifyPaidDto _notification) {
        String paymentId = _notification.getPaymentId();
        BigDecimal amount = _notification.getAmount();

        PaymentEntity paymentEntity = Optional.ofNullable(paymentRepository.findOne(paymentId))
                .orElseThrow(() -> new RecordNotFoundException("payment", paymentId));

        // 第三方支付平台反馈的支付成功金额与需要支付金额不一致。
        if (amount.compareTo(paymentEntity.getAmount()) != 0) {
            return;
        }

        paymentEntity.setStatus(PaymentStatusEnums.PAID);
        paymentRepository.save(paymentEntity);

        // 发送消息至order-service。
        PaymentSucceededPayload payload = new PaymentSucceededPayload();
        payload.setOrderId(paymentEntity.getOrderId());

        Message<PaymentSucceededPayload> message = MessageBuilder.withPayload(payload).build();
        paymentSucceededProcessor.output().send(message);
    }

    @Override
    public PageResult<PaymentDto> findPaymentsByUserId(String _userId, PageRequest _pageRequest) {
        Page<PaymentEntity> paymentEntities = paymentRepository.findByUserId(_userId, _pageRequest);

        List<PaymentDto> paymentDtos = new ArrayList<>();
        paymentEntities.forEach(
                paymentEntity -> {
                    PaymentDto paymentDto = modelMapper.map(paymentEntity, PaymentDto.class);
                    paymentDtos.add(paymentDto);
                }
        );

        PageResult<PaymentDto> pageResult = new PageResult<>();
        pageResult.setTotalElements(paymentEntities.getTotalElements());
        pageResult.setTotalPages(paymentEntities.getTotalPages());
        pageResult.setRecords(paymentDtos);

        return pageResult;
    }

}
