package com.mirror.jmarket.classes;

public class ChatRoom {
    private Item item;
    private LastMessage lastMessage;
    private String date;

    public ChatRoom() {}

    public ChatRoom(Item item, LastMessage lastMessage, String date) {
        this.item = item;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public Item getItem() {
        return item;
    }

    public LastMessage getLastMessage() { return lastMessage; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() { return date; }
}
