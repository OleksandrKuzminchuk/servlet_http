package com.sasha.servletapi.repository.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.Event;
import com.sasha.servletapi.pojo.EventData;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.util.HibernateUtil;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.stream.Collectors;

import static com.sasha.servletapi.util.constant.Constants.*;
import static com.sasha.servletapi.util.constant.SqlConstantsEvent.*;

public class EventRepositoryImpl implements EventRepository {
    private final HibernateUtil hibernateUtil = HibernateUtil.getInstance();
    @Override
    public Event save(Event event) {
        Event savedEvent;
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            savedEvent = (Event) session.merge(event);
            session.flush();
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_SAVE_A_EVENT + e);
        }
        return savedEvent;
    }

    @Override
    public Event update(Event event) {
        Event updatedEvent;
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            updatedEvent = session.get(Event.class, event.getId(), LockMode.PESSIMISTIC_WRITE);
            updatedEvent.setUser(event.getUser() != null && !event.getUser().equals(updatedEvent.getUser()) ? event.getUser() : updatedEvent.getUser());
            updatedEvent.setFile(event.getFile() != null && !event.getFile().equals(updatedEvent.getFile()) ? event.getFile() : updatedEvent.getFile());
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_UPDATE_A_EVENT_BY_ID + e.getMessage());
        }
        return updatedEvent;
    }

    @Override
    public Event findById(Integer id) {
        Event event = null;
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            Query<EventData> query = session.createQuery(FIND_EVENT_BY_ID, EventData.class);
            query.setParameter(TEXT_ID, id);
            EventData eventData = query.getSingleResult();

            if (eventData != null) {
                event = eventData.getEvent();
                User user = new User(eventData.getUserId(), eventData.getUserName());
                File file = new File(eventData.getFileId(), eventData.getFileName(), eventData.getFilePath());
                event.setUser(user);
                event.setFile(file);
            }
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_FIND_A_EVENT_BY_ID + e.getMessage());
        }
        return event;
    }


    @Override
    public List<Event> findAll() {
        List<Event> events;
        try (Session session = hibernateUtil.getSessionFactory().openSession()) {
            Query<EventData> query = session.createQuery(FIND_ALL_EVENTS, EventData.class);
            List<EventData> results = query.getResultList();

            events = results.stream().map(eventData -> {
                Event event = eventData.getEvent();
                User user = new User(eventData.getUserId(), eventData.getUserName());
                File file = new File(eventData.getFileId(), eventData.getFileName(), eventData.getFilePath());
                event.setUser(user);
                event.setFile(file);
                return event;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new NotFoundException(FAILED_TO_FIND_ALL_EVENTS + e.getMessage());
        }
        return events;
    }


    @Override
    public void deleteById(Integer id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            session
                    .createQuery(DELETE_EVENT_BY_ID)
                    .setParameter(TEXT_ID, id)
                    .executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_DELETE_A_EVENT_BY_ID + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            session
                    .createQuery(DELETE_ALL_EVENTS)
                    .executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_DELETE_ALL_EVENTS + e.getMessage());
        }
    }
}
