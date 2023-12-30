package com.mohamed.taskmanagement.service;

import com.mohamed.taskmanagement.entity.Event;

import java.util.List;

public interface IEventServcie {

    List<Event> findAllEvents();
}
