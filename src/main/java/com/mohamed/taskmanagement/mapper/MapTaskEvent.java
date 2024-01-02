package com.mohamed.taskmanagement.mapper;

import com.google.api.client.util.DateTime;
import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.entity.EventDateTime;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MapTaskEvent {

    public static Event mapGoogleEventToTaskEvent(Event taskEvent, com.google.api.services.calendar.model.Event googleEvent) {
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
                                                                                         Event taskEvent) {
        googleEvent.setId(taskEvent.getId());
        googleEvent.setSummary(taskEvent.getSummary());
        googleEvent.setDescription(taskEvent.getDescription());

        System.out.println(taskEvent.getStart().getDateTime());
        String dateTimeStr = convertToGoogleDateTimeFormat(taskEvent.getStart().getDateTime());
        System.out.println(dateTimeStr);
        DateTime startDateTime = new DateTime(dateTimeStr);
        System.out.println(startDateTime);

        com.google.api.services.calendar.model.EventDateTime dateTime1 = new com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(startDateTime);

        googleEvent.setStart(dateTime1);


        String dateTimeEnd = convertToGoogleDateTimeFormat(taskEvent.getEnd().getDateTime());
        DateTime endDateTime = new DateTime(dateTimeEnd);

        com.google.api.services.calendar.model.EventDateTime dateTime2 = new com.google.api.services.calendar.model.EventDateTime()
                .setDateTime(endDateTime);

        googleEvent.setEnd(dateTime2);
        return googleEvent;
    }

    private static String convertToGoogleDateTimeFormat(String dateTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(
                dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
        );
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);
//
         offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
//        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
//        return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

}
