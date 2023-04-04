package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.service.BaseService;
import com.sasha.servletapi.service.EventService;

import java.util.List;

public class EventServiceImpl extends BaseService implements EventService {
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, FileRepository fileRepository) {
        super(eventRepository, userRepository, fileRepository);
    }

    @Override
    public Event save(Event event) {
        isExistsFile(event.getFile().getId());
        isExistsUser(event.getUser().getId());
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        isExistsEvent(event.getId());
        isExistsFile(event.getFile().getId());
        isExistsUser(event.getUser().getId());
        return eventRepository.update(event);
    }

    @Override
    public Event findById(Integer id) {
        return isExistsEvent(id);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        isExistsEvent(id);
        eventRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        eventRepository.deleteAll();
    }
}
