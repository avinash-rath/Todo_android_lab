package com.example.todo_android_lab;


public class Model {
    private String title;
    private String description;
    private String timestamp;
    private boolean done;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimestamp(String dateTime) {
        this.timestamp = dateTime;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean getDone() {
        return done;
    }
}
