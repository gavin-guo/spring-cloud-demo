package com.gavin.base;

import java.util.List;

public interface EventPublisherTask<E> {

    List<E> pollSomeEventRecords();

    void publishEvents();

}
