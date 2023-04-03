package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.repository.impl.EventRepositoryImpl;
import com.sasha.servletapi.repository.impl.FileRepositoryImpl;
import com.sasha.servletapi.repository.impl.UserRepositoryImpl;
import com.sasha.servletapi.service.EventService;

import java.util.List;

import static com.sasha.servletapi.util.constant.Constants.*;

public class EventServiceImpl implements EventService {
    private final EventRepository repository = new EventRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final FileRepository fileRepository = new FileRepositoryImpl();
    @Override
    public Event save(Event event) {
        isExistsFile(event.getFile().getId());
        isExistsUser(event.getUser().getId());
        return repository.save(event);
    }

    @Override
    public Event update(Event event) {
        isExistsEvent(event.getId());
        isExistsFile(event.getFile().getId());
        isExistsUser(event.getUser().getId());
        return repository.update(event);
    }

    @Override
    public Event findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<Event> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        isExistsEvent(id);
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    private void isExistsEvent(Integer id) {
        if (repository.findById(id) == null) {
            throw new NotFoundException(NOT_FOUND_EVENT);
        }
    }

    private void isExistsUser(Integer id) {
        if (userRepository.findById(id) == null) {
            throw new NotFoundException(NOT_FOUND_USER);
        }
    }

    private void isExistsFile(Integer id) {
        if (fileRepository.findById(id) == null) {
            throw new NotFoundException(NOT_FOUND_FILE);
        }
    }
}
