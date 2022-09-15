package com.mirror.jmarket.classes;

public class LastMessage {
    private String message;
    private String user;
    private String time;

    public LastMessage() {}

    public LastMessage(String message, String user, String time) {
        this.message = message;
        this.user = user;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }
}
