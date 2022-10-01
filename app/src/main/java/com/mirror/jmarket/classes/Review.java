package com.mirror.jmarket.classes;

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
}
