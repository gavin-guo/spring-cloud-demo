package com.gavin.service.impl;

import com.gavin.entity.ArrangeShipmentEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.ArrangeShipmentEvent;
import com.gavin.model.PageArgument;
import com.gavin.repository.ArrangeShipmentEventRepository;
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
public class ArrangeShipmentEventServiceImpl implements EventService<ArrangeShipmentEvent> {

    private final ModelMapper modelMapper;

    private final ArrangeShipmentEventRepository arrangeShipmentEventRepository;

    @Autowired
    public ArrangeShipmentEventServiceImpl(
            ModelMapper modelMapper,
            ArrangeShipmentEventRepository arrangeShipmentEventRepository) {
        this.modelMapper = modelMapper;
        this.arrangeShipmentEventRepository = arrangeShipmentEventRepository;
    }

    @Override
    public List<ArrangeShipmentEvent> fetchEventsByStatus(MessageableEventStatusEnums _status, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        List<ArrangeShipmentEventEntity> arrangeShipmentEventEntities
                = arrangeShipmentEventRepository.findByStatusIs(MessageableEventStatusEnums.NEW, pageRequest);

        return arrangeShipmentEventEntities.stream()
                .map(arrangeShipmentEventEntity -> {
                    ArrangeShipmentEvent arrangeShipmentEvent = modelMapper.map(arrangeShipmentEventEntity, ArrangeShipmentEvent.class);
                    arrangeShipmentEvent.setOriginId(arrangeShipmentEventEntity.getId());
                    return arrangeShipmentEvent;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveEvent(ArrangeShipmentEvent _event, MessageableEventStatusEnums _status) {
        ArrangeShipmentEventEntity arrangeShipmentEventEntity = modelMapper.map(_event, ArrangeShipmentEventEntity.class);
        arrangeShipmentEventEntity.setStatus(_status);
        arrangeShipmentEventRepository.save(arrangeShipmentEventEntity);
    }

    @Override
    public void updateEventStatusById(String _Id, MessageableEventStatusEnums _status) {
        Optional.ofNullable(arrangeShipmentEventRepository.findOne(_Id))
                .ifPresent(arrangeShipmentEventEntity -> {
                    arrangeShipmentEventEntity.setStatus(_status);
                    arrangeShipmentEventRepository.save(arrangeShipmentEventEntity);
                });
    }

    @Override
    @Transactional
    public void updateEventStatusByOriginId(String _originId, MessageableEventStatusEnums _status) {
        Optional.ofNullable(arrangeShipmentEventRepository.findByOriginId(_originId))
                .ifPresent(arrangeShipmentEventEntity -> {
                    arrangeShipmentEventEntity.setStatus(_status);
                    arrangeShipmentEventRepository.save(arrangeShipmentEventEntity);
                });
    }

}
