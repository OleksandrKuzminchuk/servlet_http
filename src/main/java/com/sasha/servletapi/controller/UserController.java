package com.sasha.servletapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.sasha.servletapi.exception.NotFoundException;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.service.UserService;
import com.sasha.servletapi.service.impl.UserServiceImpl;
import com.sasha.servletapi.util.UserSerializer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.sasha.servletapi.util.constant.Constants.*;
import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(name = USER_CONTROLLER, urlPatterns = URL_API_USERS)
public class UserController extends HttpServlet {
    private final UserService service = new UserServiceImpl();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserSerializer())
            .excludeFieldsWithoutExposeAnnotation()
            .create();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        Optional<Integer> id = parseUserId(pathInfo);

        try {
            if (!id.isPresent()) {
                List<User> users = service.findAll();
                String userJson = gson.toJson(users);
                resp.setContentType(APPLICATION_JSON);
                resp.getWriter().write(userJson);
            } else {
                User user = service.findById(id.get());
                if (user != null) {
                    String userJson = gson.toJson(user);
                    resp.setContentType(APPLICATION_JSON);
                    resp.getWriter().write(userJson);
                } else {
                    sendError(resp, SC_BAD_REQUEST, format(FAILED_TO_FIND_USER_BY_ID, id));
                }
            }
        } catch (IOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = gson.fromJson(req.getReader(), User.class);
            User savedUser = service.save(user);
            String savedUserJson = gson.toJson(savedUser);
            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().write(savedUserJson);
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            Optional<Integer> id = parseUserId(pathInfo);

            if (!id.isPresent()){
                resp.sendError(SC_BAD_REQUEST, USER_ID_IS_REQUIRED);
            }

            if (pathInfo != null && pathInfo.matches(REGEX_FOLLOWED_BY_AN_INTEGER)) {
                User updateUser = gson.fromJson(req.getReader(), User.class);
                updateUser.setId(id.get());
                User updatedUser = service.update(updateUser);
                if (updatedUser != null) {
                    String updatedUserJson = gson.toJson(updatedUser);
                    resp.setContentType(APPLICATION_JSON);
                    resp.getWriter().write(updatedUserJson);
                } else {
                    resp.sendError(SC_NOT_FOUND, FAILED_TO_UPDATE_USER);
                }
            }
        }catch (NotFoundException e){
            try {
                resp.setStatus(SC_NOT_FOUND);
                resp.getWriter().write(e.getMessage());
            }catch (IOException ioException){
                System.out.println(IO_EXCEPTION + ioException);
            }
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        Optional<Integer> id = parseUserId(pathInfo);

        if (!id.isPresent()) {
            service.deleteAll();
            resp.setStatus(SC_OK);
        } else {
            try {
                service.deleteById(id.get());
                resp.setStatus(SC_OK);
            } catch (NotFoundException e) {
                sendError(resp, SC_NOT_FOUND, NOT_FOUND_USER);
            }
        }
    }

    private Optional<Integer> parseUserId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals(SLASH)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(pathInfo.substring(1)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private void sendError(HttpServletResponse resp, int errorCode, String errorMessage) {
        try {
            resp.sendError(errorCode, errorMessage);
        } catch (IOException | JsonIOException e) {
            sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST);        }
    }
}
