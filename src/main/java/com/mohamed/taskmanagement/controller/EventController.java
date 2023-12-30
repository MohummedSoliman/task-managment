package com.mohamed.taskmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
    @RequestMapping(path = "events")
public class EventController {

    @GetMapping
    public String createEvent(){
        return "event-list";
    }
}
