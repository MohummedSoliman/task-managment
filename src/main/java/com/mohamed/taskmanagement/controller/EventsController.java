package com.mohamed.taskmanagement.controller;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.mapper.MapTaskEvent;
import com.mohamed.taskmanagement.service.GoogleCalendarService;
import com.mohamed.taskmanagement.service.IEventServcie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EventsController {

    @Autowired
    private IEventServcie iEventServcie;
    @Autowired
    private GoogleCalendarService googleCalendarService;
    private static String authorizationCode = "";
    private static boolean getAuthorizationCode = false;

//    @GetMapping("/")
//    public String allEvents(Model theModel) {
//        List<Event> events = iEventServcie.findAllEvents();
//        theModel.addAttribute("events", events);
//
//        return "event-list";
//    }

    @GetMapping("login")
    public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
        return new RedirectView(googleCalendarService.authorize());
    }

    @GetMapping("event-list")
    public String allEvents(Model theModel, @RequestParam("code") String code) throws GeneralSecurityException, IOException {

        authorizationCode = code;

        if (!getAuthorizationCode) {
            googleCalendarService.getCalendarService(code);
        }

        if (!getAuthorizationCode) getAuthorizationCode = true;

        List<Event> events = iEventServcie.findAllEvents();

        theModel.addAttribute("events", events);

        return "event-list";
    }

    @GetMapping("add-event")
    public String showEventForm(Model theModel) {
        Event event = new Event();
        theModel.addAttribute("event", event);

        return "add-event";
    }

    @PostMapping("/save")
    public String saveEvent(@Valid @ModelAttribute("event") Event event, BindingResult result, Model theModel)
            throws GeneralSecurityException, IOException {

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> {
                theModel.addAttribute(fieldError.getField(), fieldError.getDefaultMessage());
            });
            return "add-event";
        }
        iEventServcie.createEvent(event);
        com.google.api.services.calendar.model.Event googleEvent = MapTaskEvent.mapTaskEventToGoogleEvent
                (new com.google.api.services.calendar.model.Event(), event);
        googleCalendarService.inserEvent(googleEvent.setId(null));
        return "redirect:/event-list?code=" + authorizationCode;
    }

    @PutMapping("update")
    public String updateEvent(@ModelAttribute("event") Event event) {
        iEventServcie.updateEvent(event);
        return "redirect:/event-list?code=" + authorizationCode;
    }

    @GetMapping("/delete")
    public String deleteEvent(@RequestParam("eventId") String id) {
        iEventServcie.deleteEventById(id);
        return "redirect:/event-list?code=" + authorizationCode;
    }

    @GetMapping("/update")
    public String showEventFormForUpdate(@RequestParam("eventId") String id, Model theModel) {

        Optional<Event> event = iEventServcie.getEventById(id);
        theModel.addAttribute("event", event.get());

        return "add-event";
    }
}
