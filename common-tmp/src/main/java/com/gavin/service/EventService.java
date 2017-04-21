package com.gavin.service;

import com.gavin.enums.MessageableEventStatusEnums;
import com.gavin.model.PageArgument;

import java.util.List;

public interface EventService<E> {

    List<E> fetchEventsByStatus(MessageableEventStatusEnums _status, PageArgument _pageArgument);

    void saveEvent(E _event, MessageableEventStatusEnums _status);

    void updateEventStatusById(String _Id, MessageableEventStatusEnums _status);

    void updateEventStatusByOriginId(String _originId, MessageableEventStatusEnums _status);

}
