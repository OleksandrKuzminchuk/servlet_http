package com.sasha.servletapi.util.constant;

public final class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String CREATE_TABLES_FILE = "classpath:/db/migration";
    public static final String HIBERNATE_PROPERTIES = "hibernate.properties";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String NOT_FOUND_USER = "Can't find the user";
    public static final String NOT_FOUND_FILE = "Can't find the file";
    public static final String NOT_FOUND_EVENT = "Can't find the event";
    public static final String FAILED_TO_SAVE_A_EVENT = "Failed to save a event: ";
    public static final String FAILED_TO_UPDATE_A_EVENT_BY_ID = "Failed to update a specialty by id - [%d]: ";
    public static final String FAILED_TO_FIND_A_EVENT_BY_ID = "Failed to find a event by id";
    public static final String FAILED_TO_FIND_ALL_EVENTS = "Failed to find all events: ";
    public static final String FAILED_TO_DELETE_A_EVENT_BY_ID = "Failed to delete a event by id - [%d]: ";
    public static final String FAILED_TO_DELETE_ALL_EVENTS = "Failed to delete all event: ";
    public static final String FAILED_TO_SAVE_USER = "Failed to save user: ";
    public static final String FAILED_TO_UPDATE_USER = "Failed to update developer: ";
    public static final String FAILED_TO_FIND_USER_BY_ID = "Failed to find user by id - [%d]";
    public static final String FAILED_TO_FIND_ALL_USERS = "Failed to find all users: ";
    public static final String FAILED_TO_DELETE_USER_BY_ID = "Failed to delete user by id - [%d]: ";
    public static final String FAILED_TO_DELETE_ALL_USERS = "Failed to delete all users: ";
    public static final String FAILED_TO_SAVE_FILE = "Failed to save file: ";
    public static final String FAILED_TO_UPDATE_FILE = "Failed to update a file";
    public static final String FAILED_TO_FIND_FILE_BY_ID = "Failed to find a skill by id - [%d]: ";
    public static final String FAILED_TO_FIND_ALL_FILES = "Failed to find all skills: ";
    public static final String FAILED_TO_DELETE_FILE_BY_ID = "Failed to delete skill by id - [%d]: ";
    public static final String FAILED_TO_DELETE_ALL_FILES = "Failed to delete all skills: ";
    public static final String PARAMETER_ID = "id";
    public static final String FAILED_DATABASE_CONNECTION = "Failed to establish database connection";
    public static final String NO_CORRECT_REQUEST = "No correct a request";
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String DATABASE_MIGRATION_SUCCESSFULLY = "Database migration completed successfully!";
    public static final String DATABASE_MIGRATION_FAILED = "Database migration failed: ";
    public static final String REGEX_FOLLOWED_BY_AN_INTEGER = "/\\d+";
    public static final String USER_CONTROLLER = "UserController";
    public static final String FILE_CONTROLLER = "FileController";
    public static final String EVENT_CONTROLLER = "EventController";
    public static final String URL_API_USERS = "/api/users/*";
    public static final String URL_API_FILES = "/api/files/*";
    public static final String URL_API_EVENTS = "/api/events/*";
    public static final String SLASH = "/";
    public static final String DIRECTORY_FILE_PACKAGE = "C:\\Users\\Kuzminchuk_Alexandr\\IdeaProjects\\servlet_http\\src\\main\\resources\\file";
    public static final String IO_EXCEPTION = "Failed to input or output stream: ";
    public static final String SERVLET_EXCEPTION = "Failed to servlet: ";
    public static final String FILE_HAS_ALREADY_TAKEN = "The file has already taken with the name. Do you want to change the name of file or overwrite it?";
    public static final String OVERWRITE = "overwrite";
    public static final String TRUE = "true";
    public static final String TEXT_FILE = "file";
    public static final String TEXT_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String TEXT_ATTACHMENT_FILENAME = "attachment; filename=";
    public static final String DOUBLE_QUOTES = "\"";
    public static final String FILE_ID_IS_REQUIRED = "File ID is required";
    public static final String USER_ID_IS_REQUIRED = "User ID is required";
    public static final String EVENT_ID_IS_REQUIRED = "Event ID is required";
    public static final String NEW_FILE_NAME_IS_REQUIRED = "New file name is required";
    public static final String FILE_NAME_HAS_ALREADY_TAKEN = "The file name is already taken. Please choose a different name.";
    public static final String FILE_USER_IS_REQUIRED = "User and File are required";
}
