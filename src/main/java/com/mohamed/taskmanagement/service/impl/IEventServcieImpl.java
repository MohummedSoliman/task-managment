package com.mohamed.taskmanagement.service.impl;

import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.repository.EventRepository;
import com.mohamed.taskmanagement.service.IEventServcie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class IEventServcieImpl implements IEventServcie {

    private EventRepository eventRepository;

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public void createEvent(Event event) {
        createNewEvent(event);
        eventRepository.save(event);
    }

    @Override
    public void deleteEventById(String id) {
        eventRepository.deleteById(id);
    }

    private Event createNewEvent(Event event){
        String eventId = generateEventId();
        event.setId(eventId);
        event.getStart().setTimeZone("Africa/Cairo");
        event.getEnd().setTimeZone("Africa/Cairo");
        LocalDate startDate = extractDateFromDateTime(event.getStart().getDateTime());
        event.getStart().setDate(startDate);
        LocalDate endDate = extractDateFromDateTime(event.getEnd().getDateTime());
        event.getStart().setDate(endDate);
        return event;
    }

    private String generateEventId() {
        String eventId = UUID.randomUUID().toString();
        return eventId;
    }

    private LocalDate extractDateFromDateTime(LocalDateTime dateTime){
        String dateStr = dateTime.toString().split("T")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return  date;
    }
}
