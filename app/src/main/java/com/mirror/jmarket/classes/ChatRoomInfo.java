package com.mirror.jmarket.classes;

import java.util.ArrayList;

public class ChatRoomInfo {
    private String user1;
    private String user2;
    private ArrayList<String> chat;

    public ChatRoomInfo() {
    }

    public ChatRoomInfo(String user1, String user2, ArrayList<String> chat) {
        this.user1 = user1;
        this.user2 = user2;
        this.chat = chat;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public ArrayList<String> getChat() {
        return chat;
    }
}
