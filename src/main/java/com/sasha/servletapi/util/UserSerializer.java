package com.sasha.servletapi.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sasha.servletapi.pojo.User;

import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", user.getId());
        jsonObject.addProperty("name", user.getName());

        JsonArray eventsArray = new JsonArray();
        if (user.getEvents() != null) {
            user.getEvents().forEach(event -> {
                JsonObject eventObject = new JsonObject();
                eventObject.addProperty("id", event.getId());

                JsonObject userObject = new JsonObject();
                userObject.addProperty("id", event.getUser().getId());
                userObject.addProperty("name", event.getUser().getName());
                eventObject.add("user", userObject);

                JsonObject fileObject = new JsonObject();
                fileObject.addProperty("id", event.getFile().getId());
                fileObject.addProperty("name", event.getFile().getName());
                fileObject.addProperty("filePath", event.getFile().getFilePath());
                eventObject.add("file", fileObject);

                eventsArray.add(eventObject);
            });
        }
        jsonObject.add("events", eventsArray);


        return jsonObject;
    }
}
