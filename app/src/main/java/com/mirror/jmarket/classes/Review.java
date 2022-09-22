package com.mirror.jmarket.classes;

public class Review {
    private Item item;
    private String review;

    public Review() {}

    public Review(Item item, String review) {
        this.item = item;
        this.review = review;
    }

    public Item getItem() {
        return item;
    }

    public String getReview() {
        return review;
    }
}
