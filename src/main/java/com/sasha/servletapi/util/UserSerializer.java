package com.sasha.servletapi.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sasha.servletapi.pojo.User;

import java.lang.reflect.Type;

import static com.sasha.servletapi.util.constant.Constants.*;

public class UserSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(TEXT_ID, user.getId());
        jsonObject.addProperty(TEXT_NAME, user.getName());

        JsonArray eventsArray = new JsonArray();
        if (user.getEvents() != null) {
            user.getEvents().forEach(event -> {
                JsonObject eventObject = new JsonObject();
                eventObject.addProperty(TEXT_ID, event.getId());

                JsonObject userObject = new JsonObject();
                userObject.addProperty(TEXT_ID, event.getUser().getId());
                userObject.addProperty(TEXT_NAME, event.getUser().getName());
                eventObject.add(TEXT_USER, userObject);

                JsonObject fileObject = new JsonObject();
                fileObject.addProperty(TEXT_ID, event.getFile().getId());
                fileObject.addProperty(TEXT_NAME, event.getFile().getName());
                fileObject.addProperty(TEXT_FILE_PATH, event.getFile().getFilePath());
                eventObject.add(TEXT_FILE, fileObject);

                eventsArray.add(eventObject);
            });
        }
        jsonObject.add(TEXT_EVENTS, eventsArray);


        return jsonObject;
    }
}
