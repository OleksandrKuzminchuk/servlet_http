package com.sasha.servletapi.repository;

import com.sasha.servletapi.pojo.File;

public interface FileRepository extends GenericRepository<File, Integer> {
    boolean isExistsByName(String name);
    File findByName(String name);
}
