package com.mohamed.taskmanagement.controller;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.mapper.MapTaskEvent;
import com.mohamed.taskmanagement.service.GoogleCalendarService;
import com.mohamed.taskmanagement.service.IEventServcie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Controller
//@RequestMapping(path = "events")
@AllArgsConstructor
public class EventController {

    private IEventServcie iEventServcie;
    private GoogleCalendarService googleCalendarService;
    private static String authorizationCode = "";
    private static boolean getAuthorizationCode = false;

    @GetMapping("event-list")
    public String createEvent(Model theModel, @RequestParam("code") String code) throws GeneralSecurityException, IOException {

        Calendar calendar;
        authorizationCode = code;

        if (!getAuthorizationCode){
            calendar = googleCalendarService.getCalendarService(code);
        } else {
            calendar = googleCalendarService.getCalendarService();
        }

        if(!getAuthorizationCode) getAuthorizationCode = true;
        List<Event> taskEvents = new ArrayList<>();

        Events events = calendar.events().list("primary").execute();
        events.getItems().forEach(event -> {
            Event newTaskEvent = new Event();
            Event taskEvent = MapTaskEvent.mapGoogleEventToTaskEvent(newTaskEvent, event);
            taskEvents.add(taskEvent);
        });
//        List<Event> events = iEventServcie.findAllEvents();

        theModel.addAttribute("events", taskEvents);

        return "event-list";
    }

    @GetMapping("login")
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
        return new RedirectView(googleCalendarService.authorize());
    }

    @GetMapping("add-event")
    public String showEventForm(Model theModle) {
        Event event = new Event();
        theModle.addAttribute("event", event);

        return "add-event";
    }

    @PostMapping("/save")
    public String saveEvent(@ModelAttribute("event") Event event) throws GeneralSecurityException, IOException {
        if (event.getId() == ""){
            save(event);
        } else {
            update(event);
        }

        return "redirect:/events/event-list?code=" + authorizationCode;
    }

    private void save(Event event) throws GeneralSecurityException, IOException {
//        event.setId(generateEventId());
        com.google.api.services.calendar.model.Event googleEvent = MapTaskEvent.mapTaskEventToGoogleEvent(new com.google.api.services.calendar.model.Event(), event);
        googleCalendarService.inserEvent(googleEvent);
    }

    private void update(Event event) throws GeneralSecurityException, IOException {
        com.google.api.services.calendar.model.Event googleEvent = MapTaskEvent.mapTaskEventToGoogleEvent(new com.google.api.services.calendar.model.Event(), event);
        googleCalendarService.updateEvent(googleEvent.getId(),googleEvent);
    }

    @GetMapping("/delete")
    public String deleteEvent(@RequestParam("eventId") String id) throws GeneralSecurityException, IOException {

        googleCalendarService.deleteEvent(id);

        return "redirect:/events/event-list?code=" + authorizationCode;
    }

    @GetMapping("/update")
    public String showEventFormForUpdate(@RequestParam("eventId") String id, Model theModel) throws IOException, GeneralSecurityException {

        com.google.api.services.calendar.model.Event googleEvent = googleCalendarService.getEventForUpdate(id);
        Event taskEvent = MapTaskEvent.mapGoogleEventToTaskEvent(new Event(), googleEvent);

//        Event event = iEventServcie.getEventById(id);
        theModel.addAttribute("event", taskEvent);

        return "add-event";
    }

    private String generateEventId() {
        String eventId = UUID.randomUUID().toString();
        return eventId;
    }
}
