package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.repository.impl.UserRepositoryImpl;
import com.sasha.servletapi.service.UserService;

import java.util.List;

import static com.sasha.servletapi.util.constant.Constants.NOT_FOUND_USER;

public class UserServiceImpl implements UserService {
    private final UserRepository repository = new UserRepositoryImpl();

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        isExistsUser(user.getId());
        return repository.update(user);
    }

    @Override
    public User findById(Integer id) {
        isExistsUser(id);
        return repository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        isExistsUser(id);
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
    private void isExistsUser(Integer id) {
        if (repository.findById(id) == null) {
            throw new NotFoundException(NOT_FOUND_USER);
        }
    }
}
