package com.gavin.service.impl;

import com.gavin.entity.UserActivatedEventEntity;
import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.event.UserActivatedEvent;
import com.gavin.model.PageArgument;
import com.gavin.repository.UserActivatedEventRepository;
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
public class UserActivatedEventServiceImpl implements EventService<UserActivatedEvent> {

    private final ModelMapper modelMapper;

    private final UserActivatedEventRepository userActivatedEventRepository;

    @Autowired
    public UserActivatedEventServiceImpl(
            ModelMapper modelMapper,
            UserActivatedEventRepository userActivatedEventRepository) {
        this.modelMapper = modelMapper;
        this.userActivatedEventRepository = userActivatedEventRepository;
    }

    @Override
    public List<UserActivatedEvent> fetchEventsByStatus(MessageableEventStatusEnums _status, PageArgument _pageArgument) {
        PageRequest pageRequest = new PageRequest(
                _pageArgument.getCurrentPage(),
                _pageArgument.getPageSize(),
                new Sort(Sort.Direction.ASC, "id"));

        List<UserActivatedEventEntity> userActivatedEventEntities
                = userActivatedEventRepository.findByStatusIs(MessageableEventStatusEnums.NEW, pageRequest);

        return userActivatedEventEntities.stream()
                .map(userActivatedEventEntity -> {
                    UserActivatedEvent UserActivatedEvent = modelMapper.map(userActivatedEventEntity, com.gavin.event.UserActivatedEvent.class);
                    UserActivatedEvent.setOriginId(userActivatedEventEntity.getId());
                    return UserActivatedEvent;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveEvent(UserActivatedEvent _event, MessageableEventStatusEnums _status) {
        if (!Optional.ofNullable(
                userActivatedEventRepository.findByUserId(_event.getUserId())).isPresent()) {
            UserActivatedEventEntity userActivatedEventEntity = modelMapper.map(_event, UserActivatedEventEntity.class);
            userActivatedEventEntity.setStatus(_status);
            userActivatedEventRepository.save(userActivatedEventEntity);
        }
    }

    @Override
    public void updateEventStatusById(String _Id, MessageableEventStatusEnums _status) {
        Optional.ofNullable(userActivatedEventRepository.findOne(_Id))
                .ifPresent(userActivatedEventEntity -> {
                    userActivatedEventEntity.setStatus(_status);
                    userActivatedEventRepository.save(userActivatedEventEntity);
                });
    }

    @Override
    public void updateEventStatusByOriginId(String _originId, MessageableEventStatusEnums _status) {
        Optional.ofNullable(userActivatedEventRepository.findByOriginId(_originId))
                .ifPresent(userActivatedEventEntity -> {
                    userActivatedEventEntity.setStatus(_status);
                    userActivatedEventRepository.save(userActivatedEventEntity);
                });
    }

}
