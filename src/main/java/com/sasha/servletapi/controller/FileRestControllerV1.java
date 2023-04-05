package com.sasha.servletapi.controller;

import com.google.gson.JsonIOException;
import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.service.FileService;
import com.sasha.servletapi.util.ServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.sasha.servletapi.util.constant.Constants.*;
import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(name = FILE_REST_CONTROLLER_V1, urlPatterns = URL_API_V1_FILES)
public class FileRestControllerV1 extends BaseRestControllerV1 {
    private final FileService service;

    public FileRestControllerV1() {
        this.service = ServiceLocator.getFileService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Optional<Integer> id = parseId(pathInfo);

            if (!id.isPresent()) {
                List<File> files = service.findAll();
                String filesJson = gson.toJson(files);
                resp.setContentType(APPLICATION_JSON);
                resp.getWriter().write(filesJson);
            } else {
                File fileMetadata = service.findById(id.get());
                if (fileMetadata != null) {
                    java.io.File file = new java.io.File(fileMetadata.getFilePath());
                    resp.setContentType(APPLICATION_OCTET_STREAM);
                    resp.setHeader(TEXT_CONTENT_DISPOSITION, TEXT_ATTACHMENT_FILENAME + DOUBLE_QUOTES + fileMetadata.getName() + DOUBLE_QUOTES);
                    Files.copy(file.toPath(), resp.getOutputStream());
                    resp.getOutputStream().flush();
                } else {
                    sendError(resp, SC_NOT_FOUND, NOT_FOUND_FILE);
                }
            }
        } catch (NotFoundException e) {
            sendError(resp, SC_BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            File file = service.uploadFile(req);

            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().write(gson.toJson(file));
        } catch (NotFoundException e) {
            sendError(resp, SC_BAD_REQUEST, e.getMessage());
        } catch (IOException | JsonIOException | ServletException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Integer id = parseId(pathInfo).orElseThrow(() -> new NotFoundException(FILE_ID_IS_REQUIRED));

            File newFileName = gson.fromJson(req.getReader(), File.class);

            if (newFileName == null || newFileName.getName().isEmpty()) {
                sendError(resp, SC_BAD_REQUEST, NEW_FILE_NAME_IS_REQUIRED);
                return;
            }

            File updatedFile = service.overwriteFile(id, newFileName.getName());

            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().write(gson.toJson(updatedFile));

        } catch (NotFoundException e) {
            sendError(resp, SC_BAD_REQUEST, e.getMessage());
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        Optional<Integer> id = parseId(pathInfo);

        if (!id.isPresent()) {
            List<File> files = service.findAll();
            files.forEach(fileMetadata -> {
                try {
                    Files.delete(Paths.get(fileMetadata.getFilePath()));
                } catch (IOException e) {
                    sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
                }
            });
            service.deleteAll();
            resp.setStatus(SC_OK);
        } else {
            try {
                File fileMetadata = service.findById(id.get());
                if (Files.deleteIfExists(Paths.get(fileMetadata.getFilePath()))) {
                    service.deleteById(id.get());
                    resp.setStatus(SC_OK);
                } else {
                    sendError(resp, SC_INTERNAL_SERVER_ERROR, format(FAILED_TO_DELETE_FILE_BY_ID, id.get()));
                }
            } catch (NotFoundException e) {
                sendError(resp, SC_BAD_REQUEST, e.getMessage());
            } catch (IOException | JsonIOException e) {
                sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST + e.getMessage());
            }
        }
    }
}
