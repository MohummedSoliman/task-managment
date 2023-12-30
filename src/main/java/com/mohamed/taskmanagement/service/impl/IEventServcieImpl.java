package com.mohamed.taskmanagement.service.impl;

import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.repository.EventRepository;
import com.mohamed.taskmanagement.service.IEventServcie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IEventServcieImpl implements IEventServcie {

    private EventRepository eventRepository;
    @Override
    public void createEvent(Event event) {
        eventRepository.save(event);
    }
}
