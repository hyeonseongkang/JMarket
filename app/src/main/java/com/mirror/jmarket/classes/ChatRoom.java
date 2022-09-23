package com.mirror.jmarket.classes;

public class ChatRoom {
    private String uid;
    private User user;
    private Item item;
    private LastMessage lastMessage;
    private boolean visited;
    private int unReadChatCount;
    private boolean leaveChatRoom;

    public ChatRoom() {}

    public ChatRoom(User user, Item item, LastMessage lastMessage, boolean visited, int unReadChatCount, boolean leaveChatRoom) {
        this.user = user;
        this.item = item;
        this.lastMessage = lastMessage;
        this.visited = visited;
        this.unReadChatCount = unReadChatCount;
        this.leaveChatRoom = leaveChatRoom;
    }


    public User getUser() { return user; }

    public void setUser(User user) { this.user = user;}

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

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setUnReadChatCount(int count) {
        this.unReadChatCount = count;
    }

    public int getUnReadChatCount() {
        return unReadChatCount;
    }

    public boolean isLeaveChatRoom() {
        return leaveChatRoom;
    }
}
