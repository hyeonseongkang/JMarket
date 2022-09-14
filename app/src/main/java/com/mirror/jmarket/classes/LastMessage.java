package com.mirror.jmarket.classes;

public class LastMessage {
    private String message;
    private String user;

    public LastMessage() {}

    public LastMessage(String message, String user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }
}
