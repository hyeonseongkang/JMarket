package com.mirror.jmarket.classes;

public class CompleteUser {
    private String sender;
    private String receiver;

    public CompleteUser() {}

    public CompleteUser(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
