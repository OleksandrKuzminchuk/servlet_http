package com.sasha.servletapi.util;

import com.sasha.servletapi.service.EventService;
import com.sasha.servletapi.service.FileService;
import com.sasha.servletapi.service.UserService;

import static com.sasha.servletapi.util.constant.Constants.TEXT_UTILITY_CLASS;

public class ServiceLocator {
    private static UserService userService;
    private static EventService eventService;
    private static FileService fileService;
    private ServiceLocator() {
        throw new IllegalStateException(TEXT_UTILITY_CLASS);
    }
    public static UserService getUserService() {
        return userService;
    }
    public static void setUserService(UserService userService) {
        ServiceLocator.userService = userService;
    }
    public static EventService getEventService() {return eventService;}
    public static void setEventService(EventService eventService) {
        ServiceLocator.eventService = eventService;
    }
    public static FileService getFileService() {
        return fileService;
    }
    public static void setFileService(FileService fileService) {
        ServiceLocator.fileService = fileService;
    }
}
