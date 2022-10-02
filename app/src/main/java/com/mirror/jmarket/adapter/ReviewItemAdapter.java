package com.mirror.jmarket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.Review;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.MyViewHolder> {

    private List<Review> reviews = new ArrayList<>();

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_reivew_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.review.setText(review.getReview());
    }

    @Override
    public int getItemCount() { return reviews == null ? 0 : reviews.size(); }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView review;

        public MyViewHolder(View itemView) {
            super(itemView);
            review = itemView.findViewById(R.id.review);
        }
    }

}
