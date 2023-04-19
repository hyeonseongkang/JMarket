package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.Review;

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
        Item item = review.getItem();
        holder.review.setText(review.getReview());
       // holder.userNickName.setText(item.getSellerName());

        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(item.getFirstPhotoUri()))
                .into(holder.photo);

    }

    @Override
    public int getItemCount() { return reviews == null ? 0 : reviews.size(); }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView review;
        private TextView userNickName;

        public MyViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            review = itemView.findViewById(R.id.review);
            userNickName = itemView.findViewById(R.id.userNickName);
        }
    }

}
