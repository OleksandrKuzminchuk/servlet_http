package com.sasha.servletapi.util.constant;

import static com.sasha.servletapi.util.constant.Constants.TEXT_UTILITY_CLASS;

public final class SqlConstantsFile {
    private SqlConstantsFile() {
        throw new IllegalStateException(TEXT_UTILITY_CLASS);
    }
    public static final String DELETE_FILE_BY_ID = "DELETE FROM File f WHERE f.id=:id";
    public static final String DELETE_ALL_FILES = "DELETE FROM File";
    public static final String FIND_ALL_FILES = "FROM File";
    public static final String IS_EXISTS_FILE_BY_NAME = "SELECT COUNT(*) FROM File f WHERE f.name=:name";
    public static final String FIND_BY_NAME = "FROM File f WHERE f.name=:name";
}
