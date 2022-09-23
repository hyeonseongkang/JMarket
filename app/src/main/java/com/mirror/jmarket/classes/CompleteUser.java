package com.mirror.jmarket.classes;

public class CompleteUser {
    private Item item;
    private String sender;
    private String receiver;
    private String seller;

    public CompleteUser() {}

    public CompleteUser(Item item, String sender, String receiver, String seller) {
        this.item = item;
        this.sender = sender;
        this.receiver = receiver;
        this.seller = seller;
    }

    public Item getItem() { return item; }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSeller() { return seller; }
}
