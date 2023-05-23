package com.mirror.jmarket.model;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class Review {
    private Item item;
    private String review;
    private String writer;

    public Review() {}

    public Review(Item item, String review, String writer) {
        this.item = item;
        this.review = review;
        this.writer = writer;
    }

    public Item getItem() {
        return item;
    }

    public String getReview() {
        return review;
    }

    public String getWriter() { return writer; }

    @BindingAdapter("itemImageUri")
    public static void loadItemImage(ImageView imageView, String photoUri) {
        Glide.with(imageView.getContext())
                .load(Uri.parse(photoUri))
                .into(imageView);
    }
}
