package com.mohamed.taskmanagement.service.impl;

import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.repository.EventRepository;
import com.mohamed.taskmanagement.service.IEventServcie;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IEventServiceImpl implements IEventServcie {

    private EventRepository eventRepository;

    private CacheManager cacheManager;

    @Override
    @Cacheable("events")
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public void createEvent(Event event) {
        createNewEvent(event);
        eventRepository.save(event);
        clearEventCache();
    }

    @Override
    public boolean deleteEventById(String id) {
        Optional<Event> event = eventRepository.findById(id);
        if (event.isPresent()){
            eventRepository.deleteById(id);
            clearEventCache();
            return true;
        }
        return false;
    }

    @Override
    public Optional<Event> getEventById(String id) {
        Optional<Event> event = eventRepository.findById(id);
        return event;
    }

    @Override
    public boolean updateEvent(Event event) {
        Optional<Event> optionalEvent = eventRepository.findById(event.getId());
        if (optionalEvent.isPresent()){
            eventRepository.save(event);
            clearEventCache();
            return true;
        }
        return false;
    }

    private Event createNewEvent(Event event){
        String eventId = generateEventId();
        event.setId(eventId);
        event.getStart().setTimeZone("Africa/Cairo");
        event.getEnd().setTimeZone("Africa/Cairo");
        LocalDate startDate = extractDateFromDateTime(event.getStart().getDateTime());
        event.getStart().setDate(startDate);
        LocalDate endDate = extractDateFromDateTime(event.getEnd().getDateTime());
        event.getEnd().setDate(endDate);
        return event;
    }

    private void clearEventCache() {
        cacheManager.getCache("events").clear();
    }

    private String generateEventId() {
        String eventId = UUID.randomUUID().toString();
        return eventId;
    }

    private LocalDate extractDateFromDateTime(String dateTime){
        String dateStr = dateTime.toString().split("T")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return  date;
    }
}
