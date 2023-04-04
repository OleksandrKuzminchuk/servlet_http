package com.sasha.servletapi.controller;

import com.google.gson.JsonIOException;
import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.service.EventService;
import com.sasha.servletapi.util.ServiceLocator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.sasha.servletapi.util.constant.Constants.*;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet(name = EVENT_REST_CONTROLLER_V1, urlPatterns = URL_API_V1_EVENTS)
public class EventRestControllerV1 extends BaseRestControllerV1 {
    private final EventService service;

    public EventRestControllerV1() {
        this.service = ServiceLocator.getEventService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Optional<Integer> id = parseId(pathInfo);

            if (!id.isPresent()) {
                List<Event> events = service.findAll();
                resp.setContentType(APPLICATION_JSON);
                resp.getWriter().write(gson.toJson(events));
            } else {
                Event event = service.findById(id.get());
                resp.setContentType(APPLICATION_JSON);
                resp.getWriter().write(gson.toJson(event));
            }
        } catch (NotFoundException e) {
            sendError(resp, SC_BAD_REQUEST, e.getMessage());
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Event event = gson.fromJson(req.getReader(), Event.class);

            if (event.getUser() == null || event.getFile() == null) {
                sendError(resp, SC_BAD_REQUEST, FILE_USER_IS_REQUIRED);
                return;
            }

            Event savedEvent = service.save(event);
            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().write(gson.toJson(savedEvent));
        } catch (NotFoundException e) {
            sendError(resp, SC_BAD_REQUEST, e.getMessage());
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        Optional<Integer> id = parseId(pathInfo);

        if (!id.isPresent()) {
            sendError(resp, SC_BAD_REQUEST, EVENT_ID_IS_REQUIRED);
            return;
        }

        try {
            Event updatedEvent = gson.fromJson(req.getReader(), Event.class);

            Event savedEvent = service.update(updatedEvent);
            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().write(gson.toJson(savedEvent));
        } catch (NotFoundException e) {
            sendError(resp, SC_BAD_REQUEST, e.getMessage());
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        Optional<Integer> id = parseId(pathInfo);

        if (!id.isPresent()) {
            service.deleteAll();
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            try {
                service.deleteById(id.get());
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (NotFoundException e) {
                sendError(resp, SC_BAD_REQUEST, e.getMessage());
            }
        }
    }
}
