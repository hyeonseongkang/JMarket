package com.mirror.jmarket.classes;

import java.util.ArrayList;

public class ChatRoom {

    private String key;
    private ArrayList<String> users;
    private ArrayList<Chat> chats;

    public ChatRoom() {
    }

    public ChatRoom(String key, ArrayList<String> users, ArrayList<Chat> chats) {
        this.key = key;
        this.users = users;
        this.chats = chats;
    }

    public String getKey() {return key; }

    public ArrayList<String> getUsers() { return users; }

    public ArrayList<Chat> getChats() {
        return chats;
    }
}
