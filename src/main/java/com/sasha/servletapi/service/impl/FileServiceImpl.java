package com.sasha.servletapi.service.impl;

import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.repository.EventRepository;
import com.sasha.servletapi.repository.FileRepository;
import com.sasha.servletapi.repository.UserRepository;
import com.sasha.servletapi.service.BaseService;
import com.sasha.servletapi.service.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.sasha.servletapi.util.constant.Constants.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileServiceImpl extends BaseService implements FileService {

    public FileServiceImpl(EventRepository eventRepository, UserRepository userRepository, FileRepository fileRepository) {
        super(eventRepository, userRepository, fileRepository);
    }

    @Override
    public File save(File file) {
        return fileRepository.save(file);
    }

    @Override
    public File uploadFile(HttpServletRequest req) throws ServletException, IOException {
        Part filePart = req.getPart(TEXT_FILE);
        String fileName = filePart.getSubmittedFileName();
        Path savePath = Paths.get(DIRECTORY_FILE_PACKAGE, fileName);

        boolean isExistsFile = isExistsByName(fileName);

        String overwriteParam = req.getParameter(TEXT_OVERWRITE);
        boolean overwrite = overwriteParam != null && overwriteParam.equals(TEXT_TRUE);

        if (isExistsFile && !overwrite) {
            throw new NotFoundException(FILE_HAS_ALREADY_TAKEN);
        } else if (overwrite && isExistsFile) {
            saveFile(filePart, savePath);
            return this.findByName(fileName);
        } else {
            saveFile(filePart, savePath);
            return this.save(new File(fileName, savePath.toString()));
        }
    }

    @Override
    public File update(File file) {
        return fileRepository.update(file);
    }

    @Override
    public File overwriteFile(Integer id, String newName) throws IOException {
        File fileMetadata = isExistsFile(id);

        if (fileMetadata.getName().equals(newName)) {
            throw new NotFoundException(FILE_NAME_HAS_ALREADY_TAKEN);
        }

        java.io.File oldFile = new java.io.File(fileMetadata.getFilePath());
        Path newPath = Paths.get(oldFile.getParent(), newName);
        Files.move(oldFile.toPath(), newPath, REPLACE_EXISTING);

        fileMetadata.setName(newName);
        fileMetadata.setFilePath(newPath.toString());

        return this.update(fileMetadata);
    }

    @Override
    public File findById(Integer id) {
        return isExistsFile(id);
    }

    @Override
    public List<File> findAll() {
        return fileRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        isExistsFile(id);
        fileRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        fileRepository.deleteAll();
    }

    @Override
    public boolean isExistsByName(String name) {
        return fileRepository.isExistsByName(name);
    }

    @Override
    public File findByName(String name) {
        return fileRepository.findByName(name);
    }

    private void saveFile(Part filePart, Path savePath) throws IOException {
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, savePath, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException(IO_EXCEPTION + e);
        }
    }
}
