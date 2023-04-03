package com.sasha.servletapi.pojo;

import java.util.Objects;

public class EventData {
    private Event event;
    private Integer userId;
    private String userName;
    private Integer fileId;
    private String fileName;
    private String filePath;

    public EventData(Integer eventId, Integer userId, String userName, Integer fileId, String fileName, String filePath) {
        this.event = new Event(eventId);
        this.userId = userId;
        this.userName = userName;
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventData)) return false;
        EventData eventData = (EventData) o;
        return Objects.equals(getEvent(), eventData.getEvent()) && Objects.equals(getUserId(), eventData.getUserId()) && Objects.equals(getUserName(), eventData.getUserName()) && Objects.equals(getFileId(), eventData.getFileId()) && Objects.equals(getFileName(), eventData.getFileName()) && Objects.equals(getFilePath(), eventData.getFilePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEvent(), getUserId(), getUserName(), getFileId(), getFileName(), getFilePath());
    }
}
