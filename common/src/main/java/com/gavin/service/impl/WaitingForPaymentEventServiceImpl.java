package com.gavin.service.impl;

import com.gavin.entity.WaitingForPaymentEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.WaitingForPaymentEvent;
import com.gavin.model.PageArgument;
import com.gavin.repository.WaitingForPaymentEventRepository;
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
public class WaitingForPaymentEventServiceImpl implements EventService<WaitingForPaymentEvent> {

    private final ModelMapper modelMapper;

    private final WaitingForPaymentEventRepository waitingForPaymentEventRepository;

    @Autowired
    public WaitingForPaymentEventServiceImpl(
            ModelMapper modelMapper,
            WaitingForPaymentEventRepository waitingForPaymentEventRepository) {
        this.modelMapper = modelMapper;
        this.waitingForPaymentEventRepository = waitingForPaymentEventRepository;
    }

    @Override
    public List<WaitingForPaymentEvent> fetchEventsByStatus(MessageableEventStatusEnums _status, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        List<WaitingForPaymentEventEntity> waitingForPaymentEventEntities
                = waitingForPaymentEventRepository.findByStatusIs(MessageableEventStatusEnums.NEW, pageRequest);

        return waitingForPaymentEventEntities.stream()
                .map(waitingForPaymentEventEntity -> {
                    WaitingForPaymentEvent waitingForPaymentEvent = modelMapper.map(waitingForPaymentEventEntity, WaitingForPaymentEvent.class);
                    waitingForPaymentEvent.setOriginId(waitingForPaymentEventEntity.getId());
                    return waitingForPaymentEvent;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveEvent(WaitingForPaymentEvent _event, MessageableEventStatusEnums _status) {
        WaitingForPaymentEventEntity waitingForPaymentEventEntity = modelMapper.map(_event, WaitingForPaymentEventEntity.class);
        waitingForPaymentEventEntity.setStatus(_status);
        waitingForPaymentEventRepository.save(waitingForPaymentEventEntity);
    }

    @Override
    public void updateEventStatusById(String _Id, MessageableEventStatusEnums _status) {
        Optional.ofNullable(waitingForPaymentEventRepository.findOne(_Id))
                .ifPresent(waitingForPaymentEventEntity -> {
                    waitingForPaymentEventEntity.setStatus(_status);
                    waitingForPaymentEventRepository.save(waitingForPaymentEventEntity);
                });
    }

    @Override
    @Transactional
    public void updateEventStatusByOriginId(String _originId, MessageableEventStatusEnums _status) {
        Optional.ofNullable(waitingForPaymentEventRepository.findByOriginId(_originId))
                .ifPresent(waitingForPaymentEventEntity -> {
                    waitingForPaymentEventEntity.setStatus(_status);
                    waitingForPaymentEventRepository.save(waitingForPaymentEventEntity);
                });
    }

}
