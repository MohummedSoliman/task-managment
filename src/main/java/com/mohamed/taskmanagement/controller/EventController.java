package com.mohamed.taskmanagement.controller;

import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.service.IEventServcie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "events")
@AllArgsConstructor
public class EventController {

    private IEventServcie iEventServcie;

    @GetMapping
    public String createEvent(Model theModel) {
        List<Event> events = iEventServcie.findAllEvents();
        theModel.addAttribute("events", events);

        return "event-list";
    }

    @GetMapping("add-event")
    public String showEventForm(){
        return "add-event";
    }
}
