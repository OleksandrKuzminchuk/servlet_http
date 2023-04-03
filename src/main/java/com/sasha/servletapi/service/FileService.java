package com.sasha.servletapi.service;

import com.sasha.servletapi.pojo.File;

public interface FileService extends GenericService<File, Integer> {
    boolean isExistsByName(String name);
    File findByName(String name);
}
