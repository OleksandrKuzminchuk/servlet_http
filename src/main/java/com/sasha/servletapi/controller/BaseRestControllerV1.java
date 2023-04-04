package com.sasha.servletapi.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.sasha.servletapi.exception.ErrorResponse;
import com.sasha.servletapi.pojo.User;
import com.sasha.servletapi.util.UserSerializer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.sasha.servletapi.util.constant.Constants.*;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public abstract class BaseRestControllerV1 extends HttpServlet {
    protected final Gson gson;
    protected BaseRestControllerV1() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserSerializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    protected void sendError(HttpServletResponse resp, int errorCode, String errorMessage) {
        try {
            resp.setContentType(APPLICATION_JSON);
            resp.getWriter().write(gson.toJson(new ErrorResponse(errorCode, errorMessage)));
        } catch (IOException | JsonIOException e) {
            try {
                resp.setContentType(APPLICATION_JSON);
                resp.getWriter().write(gson.toJson(new ErrorResponse(SC_BAD_REQUEST, NO_CORRECT_REQUEST)));
            } catch (IOException ioException) {
                sendError(resp, SC_BAD_REQUEST, NO_CORRECT_REQUEST);
            }
        }
    }

    protected Optional<Integer> parseId(String pathInfo) {
        if (pathInfo == null || pathInfo.equals(SLASH)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(pathInfo.substring(NUMBER_1)));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
