package com.sasha.servletapi.util.constant;

public final class SqlConstantsUser {
    private SqlConstantsUser() {
        throw new IllegalStateException("Utility class");
    }
    public static final String FIND_ALL_USERS =
            "SELECT u FROM User u " +
                    "LEFT JOIN FETCH u.events";
    public static final String FIND_BY_ID_USER =
            "SELECT u FROM User u " +
                    "LEFT JOIN FETCH u.events " +
                    "WHERE u.id=: id";
    public static final String DELETE_BY_ID_USER = "DELETE FROM User u WHERE u.id=:id";
    public static final String DELETE_ALL_USERS = "DELETE FROM User";
}
