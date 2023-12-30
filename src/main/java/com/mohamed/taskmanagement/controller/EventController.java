package com.mohamed.taskmanagement.controller;

import com.mohamed.taskmanagement.entity.Event;
import com.mohamed.taskmanagement.service.IEventServcie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "events")
@AllArgsConstructor
public class EventController {

    private IEventServcie iEventServcie;

    @GetMapping("event-list")
    public String createEvent(Model theModel) {
        List<Event> events = iEventServcie.findAllEvents();
        theModel.addAttribute("events", events);

        return "event-list";
    }

    @GetMapping("add-event")
    public String showEventForm(Model theModle){
        Event event = new Event();
        theModle.addAttribute("event", event);

        return "add-event";
    }

    @PostMapping("save")
    public String saveEvent(@ModelAttribute("event") Event event){
        iEventServcie.createEvent(event);
        return "redirect:/events/event-list";
    }
}
