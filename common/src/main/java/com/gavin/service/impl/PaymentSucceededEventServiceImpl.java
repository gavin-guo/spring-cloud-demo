package com.gavin.service.impl;

import com.gavin.entity.PaymentSucceededEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.PaymentSucceededEvent;
import com.gavin.model.PageArgument;
import com.gavin.repository.PaymentSucceededEventRepository;
import com.gavin.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentSucceededEventServiceImpl implements EventService<PaymentSucceededEvent> {

    private final ModelMapper modelMapper;

    private final PaymentSucceededEventRepository paymentSucceededEventRepository;

    @Autowired
    public PaymentSucceededEventServiceImpl(
            ModelMapper modelMapper,
            PaymentSucceededEventRepository paymentSucceededEventRepository) {
        this.modelMapper = modelMapper;
        this.paymentSucceededEventRepository = paymentSucceededEventRepository;
    }

    @Override
    public List<PaymentSucceededEvent> fetchEventsByStatus(MessageableEventStatusEnums _status, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        List<PaymentSucceededEventEntity> paymentSucceededEventEntities
                = paymentSucceededEventRepository.findByStatusIs(MessageableEventStatusEnums.NEW, pageRequest);

        return paymentSucceededEventEntities.stream()
                .map(paymentSucceededEventEntity -> {
                    PaymentSucceededEvent paymentSucceededEvent = modelMapper.map(paymentSucceededEventEntity, PaymentSucceededEvent.class);
                    paymentSucceededEvent.setOriginId(paymentSucceededEventEntity.getId());
                    return paymentSucceededEvent;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveEvent(PaymentSucceededEvent _event, MessageableEventStatusEnums _status) {
        PaymentSucceededEventEntity paymentSucceededEventEntity = modelMapper.map(_event, PaymentSucceededEventEntity.class);
        paymentSucceededEventEntity.setStatus(_status);
        paymentSucceededEventRepository.save(paymentSucceededEventEntity);
    }

    @Override
    public void updateEventStatusById(String _Id, MessageableEventStatusEnums _status) {
        Optional.ofNullable(paymentSucceededEventRepository.findOne(_Id))
                .ifPresent(paymentSucceededEventEntity -> {
                    paymentSucceededEventEntity.setStatus(_status);
                    paymentSucceededEventRepository.save(paymentSucceededEventEntity);
                });
    }

    @Override
    @Transactional
    public void updateEventStatusByOriginId(String _originId, MessageableEventStatusEnums _status) {
        Optional.ofNullable(paymentSucceededEventRepository.findByOriginId(_originId))
                .ifPresent(paymentSucceededEventEntity -> {
                    paymentSucceededEventEntity.setStatus(_status);
                    paymentSucceededEventRepository.save(paymentSucceededEventEntity);
                });
    }

}
