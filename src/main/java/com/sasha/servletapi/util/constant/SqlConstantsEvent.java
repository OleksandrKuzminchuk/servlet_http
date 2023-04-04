package com.sasha.servletapi.util.constant;

import static com.sasha.servletapi.util.constant.Constants.TEXT_UTILITY_CLASS;

public final class SqlConstantsEvent {
    private SqlConstantsEvent() {
        throw new IllegalStateException(TEXT_UTILITY_CLASS);
    }
    public static final String FIND_ALL_EVENTS = "SELECT NEW com.sasha.servletapi.pojo.EventData(e.id, u.id, u.name, f.id, f.name, f.filePath) FROM Event e JOIN e.user u JOIN e.file f";
    public static final String FIND_EVENT_BY_ID = "SELECT NEW com.sasha.servletapi.pojo.EventData(e.id, u.id, u.name, f.id, f.name, f.filePath) FROM Event e JOIN e.user u JOIN e.file f WHERE e.id=:id";
    public static final String DELETE_ALL_EVENTS = "DELETE FROM Event";
    public static final String DELETE_EVENT_BY_ID = "DELETE FROM Event e WHERE e.id=:id";

}
