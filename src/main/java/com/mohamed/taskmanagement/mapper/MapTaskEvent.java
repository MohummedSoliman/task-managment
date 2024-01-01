package com.mohamed.taskmanagement.mapper;

import com.google.api.client.util.DateTime;
import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.entity.EventDateTime;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MapTaskEvent {
    
    public static Event mapGoogleEventToTaskEvent(Event taskEvent, com.google.api.services.calendar.model.Event googleEvent){
        taskEvent.setId(googleEvent.getId());
        taskEvent.setSummary(googleEvent.getSummary());
        taskEvent.setDescription(googleEvent.getDescription());

        String startDateTime = googleEvent.getStart().getDateTime().toString().split("\\.")[0];
        EventDateTime start = new EventDateTime();
        start.setDateTime(startDateTime.toString());
        taskEvent.setStart(start);

        String endDateTime = googleEvent.getEnd().getDateTime().toString().split("\\.")[0];
        EventDateTime end = new EventDateTime();
        end.setDateTime(endDateTime);
        taskEvent.setEnd(end);

        return taskEvent;
    }

    public static com.google.api.services.calendar.model.Event mapTaskEventToGoogleEvent(com.google.api.services.calendar.model.Event googleEvent,
                                                                                         Event taskEvent){
        googleEvent.setId(taskEvent.getId());
        googleEvent.setSummary(taskEvent.getSummary());
        googleEvent.setDescription(taskEvent.getDescription());

        String dateTimeStr = convertToGoogleDateTimeFormat(taskEvent.getStart().getDateTime());
        DateTime startDateTime = new DateTime(dateTimeStr);
        googleEvent.setStart(new com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime));

        String dateTimeEnd = convertToGoogleDateTimeFormat(taskEvent.getEnd().getDateTime());
        DateTime endDateTime = new DateTime(dateTimeEnd);
        googleEvent.setEnd(new com.google.api.services.calendar.model.EventDateTime().setDateTime(endDateTime));

        return googleEvent;
    }

    private static String convertToGoogleDateTimeFormat(String dateTime){
        LocalDateTime localDateTime = LocalDateTime.parse(
                dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        );
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);

        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

}
