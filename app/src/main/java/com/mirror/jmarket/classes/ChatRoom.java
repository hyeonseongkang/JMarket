package com.mirror.jmarket.classes;

public class ChatRoom {
    private String key;
    private Item item;
    private String lastMessage;
    private String date;

    public ChatRoom() {}

    public ChatRoom(String key, Item item, String lastMessage, String date) {
        this.key = key;
        this.item = item;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }

    public Item getItem() {
        return item;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() { return date; }
}
