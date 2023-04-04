package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.service.BaseService;
import com.sasha.servletapi.service.UserService;

import java.util.List;

public class UserServiceImpl extends BaseService implements UserService {
    public UserServiceImpl(EventRepository eventRepository, UserRepository userRepository, FileRepository fileRepository) {
        super(eventRepository, userRepository, fileRepository);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        isExistsUser(user.getId());
        return userRepository.update(user);
    }

    @Override
    public User findById(Integer id) {
        return isExistsUser(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        isExistsUser(id);
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }
}
