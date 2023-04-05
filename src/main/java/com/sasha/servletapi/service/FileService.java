package com.sasha.servletapi.service;

import com.sasha.servletapi.pojo.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface FileService extends GenericService<File, Integer> {
    boolean isExistsByName(String name);
    File findByName(String name);
    File uploadFile(HttpServletRequest req) throws ServletException, IOException;
    File overwriteFile(Integer id, String newName) throws IOException;

}
