package com.mirror.jmarket.classes;

public class User {
    private String uid;
    private String email;
    private String password;
    private String nickName;
    private String photoUri;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String uid, String email, String password) {
        this.uid = uid;
        this.email = email;
        this.password = password;
    }

    public User(String uid, String email, String password, String nickName, String photoUri) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.nickName = nickName;
        this.photoUri = photoUri;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String uri) {
        this.photoUri = uri;
    }
}
