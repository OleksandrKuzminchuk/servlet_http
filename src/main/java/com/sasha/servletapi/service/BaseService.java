package com.sasha.servletapi.service;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.UserRepository;

import java.util.Optional;

import static com.sasha.servletapi.util.constant.Constants.*;

public abstract class BaseService {
    protected final EventRepository eventRepository;
    protected final UserRepository userRepository;
    protected final FileRepository fileRepository;

    protected BaseService(EventRepository eventRepository, UserRepository userRepository, FileRepository fileRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    protected Event isExistsEvent(Integer id) {
        return Optional.ofNullable(eventRepository.findById(id))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT));
    }

    protected User isExistsUser(Integer id) {
       return Optional.ofNullable(userRepository.findById(id))
               .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    protected File isExistsFile(Integer id) {
        return Optional.ofNullable(fileRepository.findById(id))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_FILE));
    }
}
