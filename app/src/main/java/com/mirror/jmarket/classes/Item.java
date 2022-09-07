package com.mirror.jmarket.classes;

import java.util.ArrayList;

public class Item {

    private String id;
    private String title;
    private String price;
    private String content;
    private ArrayList<String> photoKeys;
    private String key;
    private String firstPhotoUri;

    public Item() {}

    public Item(String id, String title, String price, String content, ArrayList<String> photoKeys, String key, String firstPhotoUri) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.content = content;
        this.photoKeys = photoKeys;
        this.key = key;
        this.firstPhotoUri = firstPhotoUri;
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
}
