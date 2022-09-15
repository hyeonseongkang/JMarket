package com.mirror.jmarket.classes;

public class ChatRoom {
    private String uid;
    private Item item;
    private LastMessage lastMessage;

    public ChatRoom() {}

    public ChatRoom(String uid, Item item, LastMessage lastMessage) {
        this.uid = uid;
        this.item = item;
        this.lastMessage = lastMessage;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

}
