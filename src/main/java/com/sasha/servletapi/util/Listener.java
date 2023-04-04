package com.sasha.servletapi.util;

import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.repository.impl.EventRepositoryImpl;
import com.sasha.servletapi.repository.impl.FileRepositoryImpl;
import com.sasha.servletapi.repository.impl.UserRepositoryImpl;
import com.sasha.servletapi.service.EventService;
import com.sasha.servletapi.service.FileService;
import com.sasha.servletapi.service.UserService;
import com.sasha.servletapi.service.impl.EventServiceImpl;
import com.sasha.servletapi.service.impl.FileServiceImpl;
import com.sasha.servletapi.service.impl.UserServiceImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FlywayMigration.migrate();

        EventRepository eventRepository = new EventRepositoryImpl();
        FileRepository fileRepository = new FileRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();

        EventService eventService = new EventServiceImpl(eventRepository, userRepository, fileRepository);
        UserService userService = new UserServiceImpl(eventRepository, userRepository, fileRepository);
        FileService fileService = new FileServiceImpl(eventRepository, userRepository, fileRepository);

        ServiceLocator.setEventService(eventService);
        ServiceLocator.setUserService(userService);
        ServiceLocator.setFileService(fileService);
    }
}
