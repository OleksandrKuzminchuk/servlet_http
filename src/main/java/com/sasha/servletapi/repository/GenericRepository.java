package com.sasha.servletapi.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    T save(T entity);
    T update(T entity);
    T findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void deleteAll();
}
