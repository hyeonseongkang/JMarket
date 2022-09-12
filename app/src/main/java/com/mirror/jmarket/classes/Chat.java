package com.mirror.jmarket.classes;

public class Chat {
    private String user;
    private String message;
    private String date;

    public Chat() {}

    public Chat(String user, String message, String date) {
        this.user = user;
        this.message = message;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
