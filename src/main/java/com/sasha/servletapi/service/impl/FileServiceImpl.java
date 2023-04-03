package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.impl.FileRepositoryImpl;
import com.sasha.servletapi.service.FileService;

import java.util.List;

import static com.sasha.servletapi.util.constant.Constants.NOT_FOUND_FILE;

public class FileServiceImpl implements FileService {
    private final FileRepository repository = new FileRepositoryImpl();
    @Override
    public File save(File file) {
        return repository.save(file);
    }

    @Override
    public File update(File file) {
        isExistsFile(file.getId());
        return repository.update(file);
    }

    @Override
    public File findById(Integer id) {
        isExistsFile(id);
        return repository.findById(id);
    }

    @Override
    public List<File> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        isExistsFile(id);
        repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean isExistsByName(String name) {
        return repository.isExistsByName(name);
    }

    @Override
    public File findByName(String name) {
        return repository.findByName(name);
    }

    private void isExistsFile(Integer id) {
        if (repository.findById(id) == null) {
            throw new NotFoundException(NOT_FOUND_FILE);
        }
    }
}
