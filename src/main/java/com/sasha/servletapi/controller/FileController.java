package com.sasha.servletapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.File;
import com.sasha.servletapi.service.FileService;
import com.sasha.servletapi.service.impl.FileServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.sasha.servletapi.util.constant.Constants.*;
import static java.lang.String.format;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(name = FILE_CONTROLLER, urlPatterns = URL_API_FILES)
public class FileController extends HttpServlet {
    private final FileService service = new FileServiceImpl();
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Optional<Integer> id = parseFileId(pathInfo);

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
                    resp.sendError(SC_NOT_FOUND, NOT_FOUND_FILE);
                }
            }
        }catch (NotFoundException e) {
            sendError(resp, SC_NOT_FOUND, e.getMessage());
        }catch (IOException e) {
            System.out.println(IO_EXCEPTION + e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Part filePath = req.getPart(TEXT_FILE);
            String fileName = filePath.getSubmittedFileName();
            Path savePath = Paths.get(DIRECTORY_FILE_PACKAGE, fileName);

            String overwriteParam = req.getParameter(OVERWRITE);
            boolean overwrite = overwriteParam != null && overwriteParam.equals(TRUE);
            boolean isExistsFile = service.isExistsByName(fileName);

            if (isExistsFile && !overwrite) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write(FILE_HAS_ALREADY_TAKEN);
            } else if (overwrite && isExistsFile) {
                saveFile(filePath, savePath);
                File file = service.findByName(fileName);
                resp.setContentType(APPLICATION_JSON);
                resp.getWriter().write(gson.toJson(file));
            } else {
                saveFile(filePath, savePath);
                saveFileMetadata(resp, fileName, savePath);
            }
        }catch (NotFoundException e) {
            sendError(resp, SC_NOT_FOUND, e.getMessage());
        } catch (IOException | JsonIOException e) {
            System.out.println(NO_CORRECT_REQUEST + e);
        } catch (ServletException e) {
            System.out.println(SERVLET_EXCEPTION + e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Optional<Integer> id = parseFileId(pathInfo);

            if (!id.isPresent()) {
                resp.sendError(SC_BAD_REQUEST, FILE_ID_IS_REQUIRED);
            }
            File fileMetadata = service.findById(id.get());

            File newFileName = gson.fromJson(req.getReader(), File.class);

            if (newFileName == null || newFileName.getName().isEmpty()) {
                resp.sendError(SC_BAD_REQUEST, NEW_FILE_NAME_IS_REQUIRED);
            }

            if (service.isExistsByName(newFileName.getName())) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write(FILE_NAME_HAS_ALREADY_TAKEN);
                return;
            }

            java.io.File oldFile = new java.io.File(fileMetadata.getFilePath());
            Path newPath = Paths.get(oldFile.getParent(), newFileName.getName());
            Files.move(oldFile.toPath(), newPath, REPLACE_EXISTING);

            fileMetadata.setName(newFileName.getName());
            fileMetadata.setFilePath(newPath.toString());

            File updatedFile = service.update(fileMetadata);

            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().write(gson.toJson(updatedFile));

        } catch (NotFoundException e) {
            sendError(resp, SC_NOT_FOUND, e.getMessage());
        } catch (IOException | JsonIOException e) {
            System.out.println(NO_CORRECT_REQUEST + e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        Optional<Integer> id = parseFileId(pathInfo);

        if (!id.isPresent()) {
            List<File> files = service.findAll();
            files.forEach(fileMetadata -> {
                try {
                    Files.delete(Paths.get(fileMetadata.getFilePath()));
                } catch (IOException e) {
                    System.out.println(NO_CORRECT_REQUEST);
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
                    resp.sendError(SC_INTERNAL_SERVER_ERROR, format(FAILED_TO_DELETE_FILE_BY_ID, id));
                }
            } catch (NotFoundException e) {
                sendError(resp, SC_NOT_FOUND, e.getMessage());
            } catch (IOException | JsonIOException e) {
                System.out.println(NO_CORRECT_REQUEST);
            }
        }
    }

    private Optional<Integer> parseFileId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals(SLASH)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(pathInfo.substring(1)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void saveFile(Part filePart, Path savePath) {
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, savePath, REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println(IO_EXCEPTION);
        }
    }

    private void saveFileMetadata(HttpServletResponse resp, String fileName, Path savePath) {
        try {
            File file = new File(fileName, savePath.toString());
            File savedFile = service.save(file);
            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().print(gson.toJson(savedFile));
        } catch (IOException e) {
            System.out.println(IO_EXCEPTION + e);
        }
    }

    private void sendError(HttpServletResponse resp, int errorCode, String errorMessage) {
        try {
            resp.sendError(errorCode, errorMessage);
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST);        }
    }
}
