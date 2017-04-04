package com.gavin.service.impl;

import com.gavin.entity.CancelReservationEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.CancelReservationEvent;
import com.gavin.model.PageArgument;
import com.gavin.repository.CancelReservationEventRepository;
import com.gavin.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CancelReservationEventServiceImpl implements EventService<CancelReservationEvent> {

    private final ModelMapper modelMapper;

    private final CancelReservationEventRepository cancelReservationEventRepository;

    @Autowired
    public CancelReservationEventServiceImpl(
            ModelMapper modelMapper,
            CancelReservationEventRepository cancelReservationEventRepository) {
        this.modelMapper = modelMapper;
        this.cancelReservationEventRepository = cancelReservationEventRepository;
    }

    @Override
    public List<CancelReservationEvent> fetchEventsByStatus(MessageableEventStatusEnums _status, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        List<CancelReservationEventEntity> cancelProductsReservationEventEntities
                = cancelReservationEventRepository.findByStatusIs(MessageableEventStatusEnums.NEW, pageRequest);

        return cancelProductsReservationEventEntities.stream()
                .map(cancelReservationEventEntity -> {
                    CancelReservationEvent cancelReservationEvent = modelMapper.map(cancelReservationEventEntity, CancelReservationEvent.class);
                    cancelReservationEvent.setOriginId(cancelReservationEventEntity.getId());
                    return cancelReservationEvent;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveEvent(CancelReservationEvent _event, MessageableEventStatusEnums _status) {
        CancelReservationEventEntity cancelReservationEventEntity = modelMapper.map(_event, CancelReservationEventEntity.class);
        cancelReservationEventRepository.save(cancelReservationEventEntity);
    }

    @Override
    public void updateEventStatusById(String _Id, MessageableEventStatusEnums _status) {
        Optional.ofNullable(cancelReservationEventRepository.findOne(_Id))
                .ifPresent(cancelReservationEventEntity -> {
                    cancelReservationEventEntity.setStatus(_status);
                    cancelReservationEventRepository.save(cancelReservationEventEntity);
                });
    }

    @Override
    public void updateEventStatusByOriginId(String _originId, MessageableEventStatusEnums _status) {
        Optional.ofNullable(cancelReservationEventRepository.findByOriginId(_originId))
                .ifPresent(cancelReservationEventEntity -> {
                    cancelReservationEventEntity.setStatus(_status);
                    cancelReservationEventRepository.save(cancelReservationEventEntity);
                });
    }

}
