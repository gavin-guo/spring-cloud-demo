package com.gavin.service.impl;

import com.gavin.entity.UserCreatedEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.UserCreatedEvent;
import com.gavin.model.PageArgument;
import com.gavin.repository.UserCreatedEventRepository;
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
public class UserCreatedEventServiceImpl implements EventService<UserCreatedEvent> {

    private final ModelMapper modelMapper;

    private final UserCreatedEventRepository userCreatedEventRepository;

    @Autowired
    public UserCreatedEventServiceImpl(
            ModelMapper modelMapper,
            UserCreatedEventRepository userCreatedEventRepository) {
        this.modelMapper = modelMapper;
        this.userCreatedEventRepository = userCreatedEventRepository;
    }

    @Override
    public List<UserCreatedEvent> fetchEventsByStatus(MessageableEventStatusEnums _status, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        List<UserCreatedEventEntity> userCreatedEventEntities
                = userCreatedEventRepository.findByStatusIs(MessageableEventStatusEnums.NEW, pageRequest);

        return userCreatedEventEntities.stream()
                .map(userCreatedEventEntity -> {
                    UserCreatedEvent userCreatedEvent = modelMapper.map(userCreatedEventEntity, UserCreatedEvent.class);
                    userCreatedEvent.setOriginId(userCreatedEventEntity.getId());
                    return userCreatedEvent;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveEvent(UserCreatedEvent _event, MessageableEventStatusEnums _status) {
        if (!Optional.ofNullable(
                userCreatedEventRepository.findByUserId(_event.getUserId())).isPresent()) {
            UserCreatedEventEntity userCreatedEventEntity = modelMapper.map(_event, UserCreatedEventEntity.class);
            userCreatedEventEntity.setStatus(_status);
            userCreatedEventRepository.save(userCreatedEventEntity);
        }
    }

    @Override
    public void updateEventStatusById(String _Id, MessageableEventStatusEnums _status) {
        Optional.ofNullable(userCreatedEventRepository.findOne(_Id))
                .ifPresent(userCreatedEventEntity -> {
                    userCreatedEventEntity.setStatus(_status);
                    userCreatedEventRepository.save(userCreatedEventEntity);
                });
    }

    @Override
    @Transactional
    public void updateEventStatusByOriginId(String _originId, MessageableEventStatusEnums _status) {
        Optional.ofNullable(userCreatedEventRepository.findByOriginId(_originId))
                .ifPresent(userCreatedEventEntity -> {
                    userCreatedEventEntity.setStatus(_status);
                    userCreatedEventRepository.save(userCreatedEventEntity);
                });
    }

}
