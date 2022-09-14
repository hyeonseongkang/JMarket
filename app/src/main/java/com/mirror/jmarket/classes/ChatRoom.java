package com.mirror.jmarket.classes;

public class ChatRoom {
    private String uid;
    private Item item;
    private LastMessage lastMessage;
    private String date;

    public ChatRoom() {}

    public ChatRoom(String uid, Item item, LastMessage lastMessage, String date) {
        this.uid = uid;
        this.item = item;
        this.lastMessage = lastMessage;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
