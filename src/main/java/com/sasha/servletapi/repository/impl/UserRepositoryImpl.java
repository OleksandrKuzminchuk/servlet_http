package com.sasha.servletapi.repository.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.LockMode;
import org.hibernate.Session;

import java.util.List;

import static com.sasha.servletapi.util.constant.Constants.*;
import static com.sasha.servletapi.util.constant.SqlConstantsUser.*;

public class UserRepositoryImpl implements UserRepository {
    private final HibernateUtil hibernateUtil = HibernateUtil.getInstance();
    @Override
    public User save(User user) {
        User savedUser;
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            savedUser = (User) session.merge(user);
            session.flush();
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_SAVE_USER + e.getMessage());
        }
        return savedUser;
    }

    @Override
    public User update(User user) {
        User updatedUser;
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            updatedUser = session.get(User.class, user.getId(), LockMode.PESSIMISTIC_WRITE);
            updatedUser.setName(user.getName());
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_UPDATE_USER + e.getMessage());
        }
        return updatedUser;
    }

    @Override
    public User findById(Integer id) {
        User user;
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            user = session
                    .createQuery(FIND_BY_ID_USER, User.class)
                    .setParameter(PARAMETER_ID, id)
                    .getSingleResult();
            if (user != null) {
                user.getEvents().forEach(event -> Hibernate.initialize(event.getFile()));
            }
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_FIND_USER_BY_ID + e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users;
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            users = session
                    .createQuery(FIND_ALL_USERS, User.class)
                    .getResultList();
            if (users != null) {
                users.stream()
                        .map(User::getEvents)
                        .filter(events -> !events.isEmpty())
                        .forEach(events -> events.forEach(event -> Hibernate.initialize(event.getFile())));
            }
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_FIND_ALL_USERS + e.getMessage());
        }
        return users;
    }

    @Override
    public void deleteById(Integer id) {
        try (Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            session
                    .createQuery(DELETE_BY_ID_USER)
                    .setParameter(TEXT_ID, id)
                    .executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_DELETE_USER_BY_ID + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        try(Session session = hibernateUtil.getSessionFactory().openSession()){
            session.getTransaction().begin();
            session
                    .createQuery(DELETE_ALL_USERS)
                    .executeUpdate();
            session.getTransaction().commit();
        }catch (Exception e){
            throw new NotFoundException(FAILED_TO_DELETE_ALL_USERS + e.getMessage());
        }
    }
}
