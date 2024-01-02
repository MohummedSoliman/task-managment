package com.mohamed.taskmanagement.service;

import com.mohamed.taskmanagement.entity.Event;

import java.util.List;
import java.util.Optional;

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

    /**
     *
     * @param id
     */
    boolean deleteEventById(String id);

    /**
     *
     * @param id
     * @return Event
     */
    Optional<Event> getEventById(String id);

    boolean updateEvent(Event event);
}
