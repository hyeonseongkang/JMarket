package com.mirror.jmarket.classes;

import java.util.List;

public class ChatTemp {
    List<Chat> chatList;

    public ChatTemp() {

    }

    public ChatTemp(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public List<Chat> getChatList() {
        return chatList;
    }
}
