package com.mirror.jmarket.model;

import java.util.ArrayList;

public class Item {

    private String id;
    private String title;
    private String price;
    private boolean priceOffer;
    private String content;
    private ArrayList<String> photoKeys;
    private String key;
    private String firstPhotoUri;
    private String sellerUid;
    private ArrayList<String> likes;
    private boolean salesComplete;
    private User user;

    public Item() {}

    public Item(String id, String title, String price, boolean priceOffer, String content, ArrayList<String> photoKeys, String key, String firstPhotoUri, String sellerUid, ArrayList<String> likes, boolean salesComplete) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.priceOffer = priceOffer;
        this.content = content;
        this.photoKeys = photoKeys;
        this.key = key;
        this.firstPhotoUri = firstPhotoUri;
        this.sellerUid = sellerUid;
        this.likes = likes;
        this.salesComplete = salesComplete;
        this.user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public boolean isPriceOffer() {
        return priceOffer;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getPhotoKeys() {
        return photoKeys;
    }

    public String getKey() {
        return key;
    }

    public String getFirstPhotoUri() {
        return firstPhotoUri;
    }

    public String getSellerUid() { return sellerUid; }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public boolean isSalesComplete() {
        return salesComplete;
    }

    public void setSalesComplete(boolean salesComplete) {
        this.salesComplete = salesComplete;
    }
}
