package com.mohamed.taskmanagement.service;

import com.mohamed.taskmanagement.entity.Event;

import java.util.List;

public interface IEventServcie {

    /**
     *
     * @return List of Event
     */
    List<Event> findAllEvents();

    /**
     *
     * @param event of type Event
     *
     */
    void createEvent(Event event);
}
