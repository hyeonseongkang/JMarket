package com.mirror.jmarket.model;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;

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

    public String toString() {
        return uid + " " + email + " " + password + " " + nickName + " " + photoUri;
    }

    @BindingAdapter("imageUri")
    public static void loadImage(ImageView imageView, String photoUri) {
        if (photoUri.length() <= 0) {
            Glide.with(imageView.getContext())
                    .load(R.drawable.basic_profile_photo)
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(Uri.parse(photoUri))
                    .into(imageView);
        }


    }
}
