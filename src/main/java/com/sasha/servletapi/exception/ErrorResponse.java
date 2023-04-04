package com.sasha.servletapi.exception;

import com.google.gson.annotations.Expose;

import java.util.Date;

public final class ErrorResponse {
    @Expose
    private final int errorCode;
    @Expose
    private final String massage;
    @Expose
    private final Date date;

    public ErrorResponse(int errorCode, String massage) {
        this.errorCode = errorCode;
        this.massage = massage;
        this.date = new Date();
    }

    public String getMassage() {
        return massage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Date getDate() {
        return date;
    }
}
