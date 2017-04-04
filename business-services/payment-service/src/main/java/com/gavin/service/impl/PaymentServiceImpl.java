package com.gavin.service.impl;

import com.gavin.entity.PaymentEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.enums.PaymentStatusEnums;
import com.gavin.event.PaymentSucceededEvent;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.message.producer.PaymentSucceededMessageProducer;
import com.gavin.model.PageArgument;
import com.gavin.model.dto.payment.NotifyPaidDto;
import com.gavin.model.vo.payment.PaymentVo;
import com.gavin.repository.PaymentRepository;
import com.gavin.service.EventService;
import com.gavin.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final ModelMapper modelMapper;

    private final PaymentSucceededMessageProducer paymentSucceededMessageProducer;

    private final EventService<PaymentSucceededEvent> eventService;

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(
            ModelMapper modelMapper,
            PaymentSucceededMessageProducer paymentSucceededMessageProducer,
            EventService<PaymentSucceededEvent> eventService,
            PaymentRepository paymentRepository) {
        this.modelMapper = modelMapper;
        this.paymentSucceededMessageProducer = paymentSucceededMessageProducer;
        this.eventService = eventService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void createPayment(String _userId, String _orderId, BigDecimal _amount) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setUserId(_userId);
        paymentEntity.setOrderId(_orderId);
        paymentEntity.setAmount(_amount);
        paymentEntity.setStatus(PaymentStatusEnums.CREATED);
        paymentRepository.save(paymentEntity);
    }

    @Override
    @Transactional
    public void notifiedByThirdParty(NotifyPaidDto _notification) {
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

        PaymentSucceededEvent event = new PaymentSucceededEvent();
        event.setOrderId(paymentEntity.getOrderId());

        // 向订单服务发送支付成功的消息。
        eventService.saveEvent(event, MessageableEventStatusEnums.NEW);
    }

    @Override
    public void updatePaymentStatus(String _paymentId, String _status) {
        PaymentEntity paymentEntity = Optional.ofNullable(paymentRepository.findOne(_paymentId))
                .orElseThrow(() -> new RecordNotFoundException("payment", _paymentId));

        paymentEntity.setStatus(PaymentStatusEnums.valueOf(_status));
        paymentRepository.save(paymentEntity);
    }

    @Override
    public List<PaymentVo> findPaymentsByUserId(String _userId, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        Page<PaymentEntity> paymentEntities = paymentRepository.findByUserId(_userId, pageRequest);

        List<PaymentVo> paymentVos = new ArrayList<>();
        paymentEntities.forEach(
                paymentEntity -> {
                    PaymentVo paymentVo = modelMapper.map(paymentEntity, PaymentVo.class);
                    paymentVos.add(paymentVo);
                }
        );

        _pageArgument.setTotalPages(paymentEntities.getTotalPages());
        _pageArgument.setTotalElements(paymentEntities.getTotalElements());

        return paymentVos;
    }

    @Override
    @Transactional
    public boolean publishPaymentSucceededEvent(PaymentSucceededEvent _event) {
        eventService.updateEventStatusById(
                _event.getOriginId(),
                MessageableEventStatusEnums.PUBLISHED);

        return paymentSucceededMessageProducer.sendMessage(_event);
    }

}
